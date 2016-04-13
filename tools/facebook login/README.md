keytool -exportcert -alias debug.keystore -keystore ~/.android/debug | openssl sha1 -binary | openssl base64
