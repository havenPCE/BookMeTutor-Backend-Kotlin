package com.pce.kotlin.bookmetutor.util

object Constants {
    // Basic Constants
    const val DEADLINE_HOURS = 3L
    const val SECRET_OTP_LENGTH = 6
    const val EXPIRATION_TIME = 8L
    const val SECRET = "SecretToEncrypt"
    const val TOKEN_PREFIX = "Bearer "
    const val HEADER_FIELD = "Authorization"
    const val BASE_URL = "http://localhost:5000"

    // Error Response Strings
    const val UNAUTHORIZED = "Unauthorized Access (Invalid Token)"
    const val USER_NOT_FOUND = "user not found"
    const val TUTOR_NOT_FOUND = "tutor not found"
    const val BOOKING_NOT_FOUND = "booking not found"
    const val ADDRESS_NOT_FOUND = "address id invalid"
    const val USER_EXISTS = "email id taken"
    const val SUBJECT_NOT_FOUND = "subject not found"
    const val INVALID_TOKEN = "invalid token or token expired"
    const val INVALID_REQUEST = "missing fields or invalid data received"
    const val VERIFICATION_FAILED = "invalid token, verification failed"

    //Success Response String
    const val ACCOUNT_CREATED = "Account Created Successfully, Verification Pending"
    const val AVAILABILITY_DESC = "payload => { true = available, false = not available}"
    const val ACCOUNT_VERIFIED = "Account verified successfully, You can login now"
    const val TOKEN_GENERATED = "Authorization token to access further routes"
    const val REQUEST_FULFILLED = "Request fulfilled, follow further instructions(if any)"
}

// Enum Classes to handle typing of data
enum class Gender {
    MALE,
    FEMALE
}

enum class Screening {
    PENDING,
    ACCEPTED,
    REJECTED
}

enum class BookingStatus {
    PENDING,
    ACCEPTED,
    COMPLETED,
    CANCELLED
}

enum class SubjectName {
    PHYSICS,
    CHEMISTRY,
    MATHEMATICS,
    COMPUTER,
    BIOLOGY
}

enum class Board {
    CBSE,
    ICSE
}

enum class PaymentMethod {
    DEBIT_CARD,
    CREDIT_CARD,
    INTERNET_BANKING,
    UPI
}

enum class Authority {
    STUDENT,
    TUTOR,
    ADMIN
}