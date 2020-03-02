class LcdDisplay:
    def __init__(self, gpio, time):
        self.GPIO = gpio
        self.TIME = time
        self.LCD_RS = 15
        self.LCD_E = 16
        self.LCD_D4 = 21
        self.LCD_D5 = 22
        self.LCD_D6 = 23
        self.LCD_D7 = 24
        # Define some device constants
        self.LCD_WIDTH = 16    # Maximum characters per line
        self.LCD_CHR = True
        self.LCD_CMD = False

        self.LCD_LINE_1 = 0x80  # LCD RAM address for the 1st line
        self.LCD_LINE_2 = 0xC0  # LCD RAM address for the 2nd line

        # Timing constants
        self.E_PULSE = 0.0005
        self.E_DELAY = 0.0005
        self.lcd_init()
                

    def lcd_init(self):
        #self.GPIO.setwarnings(False)
        #self.GPIO.setmode(self.GPIO.BCM)     # Use BCM GPIO numbers
        self.GPIO.setup(self.LCD_E, self.GPIO.OUT)  # E
        self.GPIO.setup(self.LCD_RS, self.GPIO.OUT)  # RS
        self.GPIO.setup(self.LCD_D4, self.GPIO.OUT)  # DB4
        self.GPIO.setup(self.LCD_D5, self.GPIO.OUT)  # DB5
        self.GPIO.setup(self.LCD_D6, self.GPIO.OUT)  # DB6
        self.GPIO.setup(self.LCD_D7, self.GPIO.OUT)  # DB7
        # Initialise display
        self.lcd_byte(0x33, self.LCD_CMD)  # 110011 Initialise
        self.lcd_byte(0x32, self.LCD_CMD)  # 110010 Initialise
        self.lcd_byte(0x06, self.LCD_CMD)  # 000110 Cursor move direction
        self.lcd_byte(0x0C, self.LCD_CMD)  # 001100 Display On,Cursor Off, Blink Off
        self.lcd_byte(0x28, self.LCD_CMD)  # 101000 Data length, number of lines, font size
        self.lcd_byte(0x01, self.LCD_CMD)  # 000001 Clear display
        self.TIME.sleep(self.E_DELAY)

    def lcd_byte(self, bits, mode):
      
        self.GPIO.output(self.LCD_RS, mode)
        self.GPIO.output(self.LCD_D4, False)
        self.GPIO.output(self.LCD_D5, False)
        self.GPIO.output(self.LCD_D6, False)
        self.GPIO.output(self.LCD_D7, False)
        if bits & 0x10 == 0x10:
            self.GPIO.output(self.LCD_D4, True)
        if bits & 0x20 == 0x20:
            self.GPIO.output(self.LCD_D5, True)
        if bits & 0x40 == 0x40:
            self.GPIO.output(self.LCD_D6, True)
        if bits & 0x80 == 0x80:
            self.GPIO.output(self.LCD_D7, True)

        # Toggle 'Enable' pin
        self.lcd_toggle_enable()
         # Low bits
        self.GPIO.output(self.LCD_D4, False)
        self.GPIO.output(self.LCD_D5, False)
        self.GPIO.output(self.LCD_D6, False)
        self.GPIO.output(self.LCD_D7, False)
        if bits & 0x01 == 0x01:
            self.GPIO.output(self.LCD_D4, True)
        if bits & 0x02 == 0x02:
            self.GPIO.output(self.LCD_D5, True)
        if bits & 0x04 == 0x04:
            self.GPIO.output(self.LCD_D6, True)
        if bits & 0x08 == 0x08:
            self.GPIO.output(self.LCD_D7, True)
        # Toggle 'Enable' pin
        self.lcd_toggle_enable()


    def lcd_toggle_enable(self):
        # Toggle enable
        self.TIME.sleep(self.E_DELAY)
        self.GPIO.output(self.LCD_E, True)
        self.TIME.sleep(self.E_PULSE)
        self.GPIO.output(self.LCD_E, False)
        self.TIME.sleep(self.E_DELAY)

    def lcd_string(self, message, line):
        # Send string to display
        message = message.ljust(self.LCD_WIDTH, " ")
        self.lcd_byte(line, self.LCD_CMD)

        for i in range(self.LCD_WIDTH):
            self.lcd_byte(ord(message[i]), self.LCD_CHR)


    def greetings(self):
        self.show("Welcome To", "GMS")
        self.TIME.sleep(1)
        self.show("getting", "ready..")
        self.TIME.sleep(1)


    def show(self, msg1, msg2):
        self.lcd_byte(self.LCD_LINE_1, self.LCD_CMD)
        self.lcd_string(msg1, self.LCD_LINE_1)
        self.lcd_byte(self.LCD_LINE_2, self.LCD_CMD)
        self.lcd_string(msg2, self.LCD_LINE_2)


    def show_upper_line(self, msg):
        self.lcd_byte(self.LCD_LINE_1, self.LCD_CMD)
        self.lcd_string(msg, self.LCD_LINE_1)


    def show_lower_line(self, msg):
        self.lcd_byte(self.LCD_LINE_2, self.LCD_CMD)
        self.lcd_string(msg, self.LCD_LINE_2)