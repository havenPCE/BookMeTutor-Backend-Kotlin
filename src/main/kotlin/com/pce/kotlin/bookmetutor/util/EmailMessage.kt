package com.pce.kotlin.bookmetutor.util

fun makeVerifyRequest(email: String = "", token: String = "", role: Authority = Authority.STUDENT): Pair<String, String> = Pair("Request for ${role.name} account verification",
        """"
                    |<h4>Thank you for joining us
                    |
                    |Please verify your email using the button below
                    |<form action="${Constants.BASE_URL}/account/verify?email=${email}&jwt=${token}&role=${role.name}"><button type="submit">Verify Account</button></form></h4>
                    """".trimMargin()
)

fun makeResetRequest(email: String = "", token: String = "", role: Authority = Authority.STUDENT): Pair<String, String> = Pair("Request for ${role.name} password reset",
        """
            |<h4>Here is the link to button to confirm your new password
            |
            |Please click on the button below
            |<form action="${Constants.BASE_URL}/account/reset?email=${email}&jwt=${token}&role=${role.name}"><button type="submit">Confirm Reset</button></form></h4>
        """.trimMargin()
)