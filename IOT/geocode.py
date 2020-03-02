
class Geocode:
    def __init__(self, requests):
        self.RES = requests.get('https://ipinfo.io/')



    def trackLocation(self):
        data = self.RES.json()
        city = data['city']
        location = data['loc'].split(',')
        latitude = location[0]
        longitude = location[1]
        return {'city': city, 'latitude': latitude, 'longitude': longitude}
