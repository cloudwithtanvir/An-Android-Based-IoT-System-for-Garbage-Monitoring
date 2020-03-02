import math
import socket
import sys
import threading
import time
from datetime import datetime

import RPi.GPIO as GPIO

from geocode import Geocode

import requests

import rethinkdb as r

from sensor import Sensor
from lcd import LcdDisplay

from twilio.rest import Client
from configparser import ConfigParser

config = ConfigParser()
config.read('./../config.ini')

db_host = config['DEFAULT']['DATABASE_HOST']

conn = r.connect(db_host, 28015)
conn.use('pi')

GPIO.setwarnings(False)
GPIO.cleanup()
GPIO.setmode(GPIO.BOARD)
lcd = LcdDisplay(GPIO, time)

cleaningFlag = {"garbageExist":False,"inactiveOccured":False, "activeOccured":False}
totalCleaned = 0
isRunning = True
tune = True
press = 0

def database_change(id):
    global db_host
    print("Database Thread Started")
    conn2 = r.connect(db_host, 28015)
    conn2.use('pi')
    cursor = r.table("bin").filter(r.row['id'] == id).changes().run(conn2)
    for document in cursor:
        if document['new_val']['status'] == 'inactive':
            print("Sensor is paused")
            change_running_state(False)
            cleaningFlag["inactiveOccured"] = True

        elif document['new_val']['status'] == 'active':
            print("sensor is running")
            change_running_state(True)
            cleaningFlag["activeOccured"] = True 

        if document['new_val']['tuned'] == False:
            tune_now()

        if document['new_val']['current_level'] >= document['new_val']['notify_level']:
            GPIO.output(int(document['new_val']['led']), GPIO.HIGH)
        else:
            GPIO.output(int(document['new_val']['led']), GPIO.LOW)

def button_press_listener(pin, id):
    global isRunning
    while True:
        input_state = GPIO.input(int(pin))
        if input_state == False:
            if isRunning:
                print("Sensor is paused")
                change_running_state(False)
                update_bin_info(id,'status','inactive')
                cleaningFlag["inactiveOccured"] = True
            else:
                print("sensor is running")
                change_running_state(True)
                update_bin_info(id, 'status', 'active')
                cleaningFlag["activeOccured"] = True
            time.sleep(1)

            
def show_on_lcd(current_level, bin_id):
    pass




def change_running_state(state):
    global isRunning
    isRunning = state

def tune_now():
    global tune 
    tune = False

def update_bin_info(id, attrib, info):
    r.table('bin').get(id).update({attrib : info}).run(conn)

def update_bin_location(id,latitude, longitude):
    r.table('bin').get(id).update({"latitude": latitude, "longitude":longitude}).run(conn)

def clean_counter(id, current_level):
    global totalCleaned
    global cleaningFlag
    if current_level == 0 and (cleaningFlag["garbageExist"] and cleaningFlag["inactiveOccured"] and cleaningFlag["activeOccured"]) is True:
        totalCleaned += 1
        update_bin_info(id, 'count', totalCleaned)
        update_bin_info(id, 'last_cleaned', datetime.now().strftime('%d-%b-%y'))
        cleaningFlag = {"garbageExist": False, "inactiveOccured": False, "activeOccured": False}


def run_sensor(trash):
    global isRunning
    global tune
    geocode = Geocode(requests)
    location = geocode.trackLocation()
    update_bin_location(trash.ID, location["latitude"], location["longitude"])

    while True:
        if isRunning:
            if (trash.TUNED and tune) is True:
                trash.measureDistance(GPIO, time)
                time.sleep(2)
                current_level = trash.garbage_calc()
                if current_level > 30:
                    cleaningFlag["garbageExist"] = True
                update_bin_info(trash.ID, 'current_level', current_level)
                clean_counter(trash.ID, current_level)
                

            else:
                print("Bin Not Tuned. Tunnning Now....")
                trash.tune_sensor(GPIO, time)
                update_bin_info(trash.ID, 'depth', trash.DEPTH)
                update_bin_info(trash.ID, 'tuned', True)
                tune = True
                print("Bin Depth %d" % trash.DEPTH)

def main():
    global totalCleaned 
    global isRunning
    sensor_id = sys.argv[1]
    bin = r.table('bin').get(sensor_id).run(conn)
    if bin is not None:
        if bin["status"] =="inactive":
            isRunning = False
        trash = Sensor.from_database(bin, GPIO)
        GPIO.setup(int(bin['button']), GPIO.IN, pull_up_down=GPIO.PUD_UP) 
        GPIO.setup(int(bin['led']), GPIO.OUT)
        totalCleaned = int(bin["count"])
        d_thread = threading.Thread(name='database_thread', target=database_change, kwargs={'id': bin['id']})
        b_thread = threading.Thread(name='button_press_listener', target=button_press_listener, kwargs={'pin': bin['button'], 'id': bin['id']})
        d_thread.start()
        b_thread.start()
        run_sensor(trash)
    
if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        pass
    finally:
        # lcd.show("gms", "shutting down..")
        GPIO.cleanup()
        print("GMS Stopped")
