# Transport management app #

1. Add a truck: 
 - a) localhost:8080/trucks/add,
 - b) Set: licensePlate, capacity and inspectionDate for truck entity.

2. Add a driver:
 - a) localhost:8080/drivers/add,
 - b) Set: name, lastName, contactNumber and email for driver entity.

**You can also set driver and truck coordinates**  

For example:
- localhost:8080/trucks/coordinates/{truckId}

 
"x": 1,  
"y": 1  
- localhost:8080/drivers/coordinates/{driverId}
 
 "x": 1,  
 "y": 1

3. Create a route:
  - a) localhost:8080/roads/add,
  - b) Set from, via, to, departureDate and arrivalDate for road entity.

Open browser and type http://localhost:8080/location/map and enjoy.
