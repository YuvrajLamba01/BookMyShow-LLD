# BookMyShow - Class Diagram

```mermaid
classDiagram
    class User {
      -userId: String
      -name: String
      -email: String
    }

    class Movie {
      -movieId: String
      -title: String
      -durationMins: int
      -language: String
      -genre: String
    }

    class Theater {
      -theaterId: String
      -name: String
      -city: String
      -screens: List~Screen~
      +addScreen(screen)
    }

    class Screen {
      -screenId: String
      -name: String
      -rows: int
      -cols: int
      +seatIds(): List~String~
    }

    class Show {
      -showId: String
      -movie: Movie
      -screen: Screen
      -theater: Theater
      -startTime: LocalDateTime
      -pricePerSeat: double
      -availableSeats: Set~String~
      +isRunning(now): boolean
    }

    class SeatLock {
      -userId: String
      -showId: String
      -seats: Set~String~
      -expiresAt: LocalDateTime
      +isExpired(now): boolean
    }

    class Booking {
      -bookingId: String
      -user: User
      -show: Show
      -seats: List~String~
      -totalAmount: double
      -status: BookingStatus
      +confirm()
      +cancel()
      +fail()
    }

    class Payment {
      -paymentId: String
      -bookingId: String
      -amount: double
      -status: PaymentStatus
      +pay()
      +refund()
    }

    class CatalogService {
      +searchByMovie(city, title): List~Show~
      +searchByGenre(city, genre): List~Show~
    }

    class BookingService {
      +lockSeats(user, showId, seats): boolean
      +confirmBooking(user, showId): Optional~Booking~
      +cancelBooking(bookingId): boolean
      +releaseExpiredLocks()
    }

    class PaymentService {
      +payForBooking(booking): Payment
      +refundBooking(booking): Payment
    }

    class ShowRepository {
      <<interface>>
      +findById(showId): Optional~Show~
      +findAll(): List~Show~
      +save(show)
    }

    class BookingRepository {
      <<interface>>
      +findById(bookingId): Optional~Booking~
      +save(booking)
    }

    class SeatLockRepository {
      <<interface>>
      +findByShowId(showId): List~SeatLock~
      +findByShowIdAndUserId(showId, userId): Optional~SeatLock~
      +save(showId, lock)
      +removeByShowIdAndUserId(showId, userId)
      +replaceAll(showId, locks)
    }

    class PaymentGateway {
      <<interface>>
      +charge(bookingId, amount): boolean
      +refund(bookingId, amount): boolean
    }

    class InMemoryShowRepository
    class InMemoryBookingRepository
    class InMemorySeatLockRepository
    class MockPaymentGateway

    class BookingStatus {
      <<enumeration>>
      PENDING
      CONFIRMED
      CANCELLED
      FAILED
    }

    class PaymentStatus {
      <<enumeration>>
      INITIATED
      SUCCESS
      FAILED
      REFUNDED
    }

    Theater "1" o-- "*" Screen
    Show "*" --> "1" Movie
    Show "*" --> "1" Screen
    Booking "*" --> "1" User
    Booking "*" --> "1" Show
    Payment "*" --> "1" Booking
    BookingService ..> SeatLock
    CatalogService ..> Show
    PaymentService ..> Payment
    CatalogService ..> ShowRepository
    BookingService ..> ShowRepository
    BookingService ..> BookingRepository
    BookingService ..> SeatLockRepository
    PaymentService ..> PaymentGateway
    InMemoryShowRepository ..|> ShowRepository
    InMemoryBookingRepository ..|> BookingRepository
    InMemorySeatLockRepository ..|> SeatLockRepository
    MockPaymentGateway ..|> PaymentGateway
```

## Notes
- `Show.availableSeats` tracks all unbooked seats.
- `SeatLock` prevents race conditions for concurrent users.
- Booking is a two-step flow: lock seats -> pay -> confirm.
