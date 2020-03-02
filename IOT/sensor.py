import math

class Sensor:
    # Sound Velocity in centi meter
    SOUND_VELOCITY = 17150

    def __init__(self, id, name, trig, echo, depth, tuned, gpio):
        self.ID = id
        self.NAME = name
        self.DISTANCE = 0
        self.TRIG = trig
        self.ECHO = echo
        self.DEPTH = depth
        self.TUNED = tuned
        self.initSensor(gpio)

    @classmethod
    def from_database(cls, bin, gpio):
        id = bin["id"]
        name = bin["name"]
        trig = int(bin['trig_pin'])
        echo = int(bin['echo_pin'])
        depth = int(bin["depth"])
        tuned = bin["tuned"]
        return cls(id, name, trig, echo, depth, tuned, gpio)

    def initSensor(self, GPIO):
        GPIO.setup(self.ECHO, GPIO.IN)
        GPIO.setup(self.TRIG, GPIO.OUT)

    def getSensorId(self):
        return self.ID

    def getDistance(self):
        return self.DISTANCE

    def measureDistance(self, GPIO, TIME):
        GPIO.output(self.TRIG, GPIO.LOW)
        TIME.sleep(0.1)
        GPIO.output(self.TRIG, GPIO.HIGH)
        TIME.sleep(0.00001)
        GPIO.output(self.TRIG, GPIO.LOW)
        while GPIO.input(self.ECHO) == False:
            pulse_start = TIME.time()
        while GPIO.input(self.ECHO) == True:
            pulse_end = TIME.time()
        pulse_duration = pulse_end - pulse_start
        distance = pulse_duration * self.SOUND_VELOCITY
        self.DISTANCE = distance

    def garbage_calc(self):
        print("Distance %d" % self.DISTANCE)
        print("Trash Height %d" % self.DEPTH)
        garbage_percent = (1 - math.floor(self.DISTANCE) / self.DEPTH) * 100
        garbage_percent = 0 if garbage_percent < 0 else garbage_percent
        print(garbage_percent)
        return round(garbage_percent)

    def tune_sensor(self, GPIO, TIME):
        total_distance = 0
        total_reading = 10
        for _ in range(10):
            self.measureDistance(GPIO, TIME)
            total_distance += self.DISTANCE
            TIME.sleep(0.05)
        self.TUNED = True
        self.DEPTH = math.floor(total_distance / total_reading)
