# hotel-flights-reservation
This structure can provide a simple Hotel and Flight reservation.

# considerations to continuous improvement:
- The object's used to make a reservation, BookingDTO and FlightReservationDTO, will could implement a ReservationDTO, in order to avoid redundant information between both objects.
  - The same can be applied to Response object.
- The structure of the repository was inspired by Spring JPA.
