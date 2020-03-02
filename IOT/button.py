import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BCM)

GPIO.setup(18, GPIO.IN, pull_up_down=GPIO.PUD_UP)
press = 0
while True:
    input_state = GPIO.input(18)
    if input_state == False:
        press += 1
        print('Button Pressed %d' % press)
        time.sleep(1)

