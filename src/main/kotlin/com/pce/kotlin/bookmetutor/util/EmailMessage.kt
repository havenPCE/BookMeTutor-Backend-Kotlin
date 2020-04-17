package com.pce.kotlin.bookmetutor.util

fun makeVerifyRequest(email: String = "", token: String = "", role: Authority = Authority.STUDENT): Pair<String, String> = Pair("Request for ${role.name} account verification",
        """
                    |<h4>Thank you for joining us
                    |
                    |
                    |Please verify your email using the button below
                    |
                    |<a href="${BASE_URL}/account/verify?email=${email}&jwt=${token}&role=${role.name}"><button type="button">Verify Account</button></a></h4>
                    """.trimMargin().h4()
)

fun makeResetRequest(email: String = "", token: String = "", role: Authority = Authority.STUDENT): Pair<String, String> = Pair("Request for ${role.name} password reset",
        """
            |Here is the link to button to confirm your new password
            |
            |
            |Please click on the button below
            |
            |<a href="${BASE_URL}/account/reset?email=${email}&jwt=${token}&role=${role.name}"><button type="button">Confirm Reset</button></a>
        """.trimMargin().h4()
)

fun makeAcceptEmail(bookingId: Long) = Pair(
        "Notice for acceptance of booking",
        """
            Thank you for accepting the booking with id: ${bookingId}.
        """.trimIndent().h4()
)

fun makeApology(firstName: String) = Pair(
        "Notice for your booking",
        """
            Sorry, $firstName,
            We couldn't find a tutor for your booking. We apologize for the inconvenience.
            
            Your amount will be refunded to your account within 1-2 business days.
            
            For further help regarding our services please contact us using the site page.
        """.trimIndent().h4()
)

fun makeAvailableEmail(bookingId: Long, firstName: String) = Pair(
        "Notice For Assigned Booking",
        """
            Hello $firstName, there is a booking with id: $bookingId available for you, 
                Please check it out in your dashboard.
                
                For further help regarding our services please contact us using the site page.
        """.trimIndent().h4()
)

fun makeBookingChangeMail(bookingId: Long) = Pair(
        "Notice For Assigned Booking",
        """
            Hello ,
            Booking no.$bookingId that is assigned to you has been changed. It may have been rescheduled or updated its status.
            Please review immediately.
            
            For further help regarding our services please contact us using the site page.
        """.trimIndent().h4()
)

fun makeThanksMail(bookingId: Long, firstName: String) = Pair(
        "Notice For Your Booking",
        """
            Thank You $firstName for using our service,
            We have assigned a tutor for your booking(id: $bookingId).
            
            We'll update you again once they accept the booking shortly. 
            
            For further help regarding our services please contact us using the site page.
        """.trimIndent().h4()
)

fun makeAcceptMailStudent(bookingId: Long) = Pair(
        "Notice For your booking",
        """
            Hello, 
            A tutor has accepted your booking#${bookingId}, they will contact you shortly before the scheduled session.
        """.trimIndent()
)