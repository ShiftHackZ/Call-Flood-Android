# Call-Flood-Android

[![Version](https://img.shields.io/badge/Version-0.0.2-blue)](https://github.com/ShiftHackZ/Call-Flood-Android/releases)

This is the Android application that allows to perform GSM Call flood attacks, causing denial of service for particular GSM consumer (so noone is able to get through attacked number) or just to annoy the owner of atacked number.

### Warning

This software should be used only for demonstration purpose. Usage of this software to attack number that does not belongs you or without exact permission of consumer may be prohibited by law in your contry. Author of this software is not responsible for the actions of the user of this software.

### Attack guide

1. Get a separate phone with a SIM card that does not registered with your identity. (Optional, but recommended)
2. Setup number anti-determiner service, so consumer can not block you. (Optional, but recommended)
3. On your smartphone, unlock your developer mode settings, and toggle "Stay awake" option to on. This is needed to prevent screen auto-lock after flood is started.
4. Connect your phone to the power supply, because "Stay awake" option will work only while charging.
5. Launch the program, on the first launch you should accept all the permissions, or otherwise software will not work.
6. Enter the phone number, also you can adjust some parameters.
7. Press "Start flood" button. The call starts immediately, and finished after time set in "Max duration" parameter. All the next calls are launched with "Start delay" time parameter.
8. To stop the flood finish the call, and press "Stop flood" button

## Future plans
- [ ] Implement selection of the number from the contacts
- [ ] Fix the timers synchronization (in case system PHONE_CALL activity is interrupted)

## Donate

The donation is optional, if you'd like to say thanks and show a little support, here is the button:

[!["Buy Me A Coffee"](https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png)](https://www.buymeacoffee.com/shifthackz)
