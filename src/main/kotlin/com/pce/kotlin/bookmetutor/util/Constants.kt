package com.pce.kotlin.bookmetutor.util

// Basic Constants
const val DEADLINE_HOURS = 3L
const val SECRET_OTP_LENGTH = 6
const val EXPIRATION_TIME = 28800000L
const val SECRET = "SecretToEncrypt"
const val TOKEN_PREFIX = "Bearer "
const val HEADER_FIELD = "Authorization"
const val BASE_URL = "http://localhost:5000"

// Error Response Strings
const val UNAUTHORIZED = "Unauthorized Access (Invalid Token)"
const val ADMIN_NOT_FOUND = "Admin not found"
const val STUDENT_NOT_FOUND = "Student not found"
const val TUTOR_NOT_FOUND = "Tutor not found"
const val BOOKING_NOT_FOUND = "Booking not found"
const val ADDRESS_NOT_FOUND = "Address id invalid"
const val ACCOUNT_EXISTS = "Email id taken"
const val SUBJECT_NOT_FOUND = "Subject not found"
const val INVALID_TOKEN = "Invalid token or token expired"
const val INVALID_REQUEST = "Missing fields or invalid data received"
const val VERIFICATION_FAILED = "Invalid token, verification failed"
const val TASK_FAILED = "Internal Error, Task Couldn't be completed"
const val INVALID_CREDENTIALS = "Invalid email or password"
const val NOT_VERIFIED = "Account not verified"
const val SECRET_UNMATCHED = "The provided secret doesn't match"

//Success Response String
const val ACCOUNT_CREATED = "Account Created Successfully, Verification Pending"
const val AVAILABILITY_DESC = "payload => { true = available, false = not available}"
const val ACCOUNT_VERIFIED = "Account verified successfully, You can login now"
const val TOKEN_GENERATED = "Authorization token to access further routes"
const val TASK_SUCCESSFUL = "Task Completed Successfully"
const val ACCOUNT_DISABLED = "Account has been disabled"

//Payload Information
const val ADMIN_INFO = "Admin information"
const val TUTOR_INFO = "Tutor information"
const val STUDENT_INFO = "Student information"
const val SUBJECT_INFO = "Subject information"
const val PHONE_INFO = "List of phone numbers"
const val ADDRESS_INFO = "Address information"
const val BOOKING_INFO = "Booking information"
const val QUALIFICATION_INFO = "Qualification information"

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