package dev.saha.otpauthserverweb.util;

public class MessageTemplate {

    private static final String messageHead = " <meta charset=\"UTF-8\" />\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
            "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC\" crossorigin=\"anonymous\"/>\n" +
            "    <!-- jQuery Library -->\n" +
            "    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js\"></script>\n" +
            "    <!-- Popper JS -->\n" +
            "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js\"></script>\n" +
            "    <!-- Latest Compiled JavaScript -->\n" +
            "    <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js\"></script>\n" +
            "    <title>SEAICO MAIL</title>";

    private static final String messageBody = "<div class=\"container\" style=\"margin-top: 2px; justify-content: center;\">\n" +
            "      <div>\n" +
            "        <div>\n" +
            "            <div class=\"background-container\">\n" +
            "            <div style=\"font-size: 14px; font-family: 'Verdana', Geneva, Tahoma, sans-serif; padding: 20px;\">\n" +
            "            <p>\n" +
            "              <span style=\"color: teal; font-weight: 700; font-size: 19px;\">Hi [[name]] !!! </span> Your multi-factor authentication to Calypso has been enabled." +
            "               You can visit the url [[url]] to update your password before being able to log in " +
            "            </p>\n" +
//            "            <p>That's worth a great celebration,<img src=\"https://www.pngarts.com/files/3/Flowers-Download-PNG-Image.png\" width=\"9%x\" height=\"5%\"/>\n" +
//            "            </p></div>\n" +
            "            <div>\n" +
            "                <h3 style=\"color: rgba(0,0,0,0.6); font-weight:700; padding: 20px 0; width: 100%; font-size: 19px;\">Your default password is [[defaultPassword]] and your userName is [[userName]]</h3><h2>" +
            "           your new password must be up to 8 characters and must contain at least a digit, a special character, an uppercase character and a lower case character</h2></div>\n"+
            "            </div>\n" +
            "          </div>\n" +
            "        </div>\n" +
            "      </div>\n" +
            "    </div>";

    public static String  welcomeMessage =

            "<!DOCTYPE html> "+"\n"+
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    messageHead+
                    "</head>\n" +
                    "  <body>\n" +
                    messageBody+
                    "  </body>\n" +
                    "</html>";

    public static final String welcomeMessageSubject = "SEAICO 2FA ENABLE!!";

}
