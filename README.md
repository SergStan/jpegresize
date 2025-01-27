Test task

Application:
Visualizer of JPEG image quality loss when changing the compression level.

Goal:
Show the user how much the JPEG image file size will decrease and how much the quality of this image will drop with different compression levels.

Language:
Kotlin

The application should consist of three screens:

Each screen should be implemented as a separate activity. Each screen should "survive" the configuration change, i.e. flip the screen with restarting the activity.

First screen.

Consists of the following components:
1. Button "Select photo"

2. Button "Next"

3. Display area of ​​the selected photo

When you click "Select photo", the system method of selecting a photo from the gallery should work. The selected photo should be displayed in the corresponding area on the screen.

Second screen.
Consists of the following components:
1. Preview area, where the previously selected photo is displayed in the appropriate quality

2. Information display area

3. Slider with values ​​from 1 to 100

4. “Next” button

The slider value corresponds to the compression level of the JPEG image. When changing the slider value, the user should see in real time how the image quality will change with the appropriate compression level. Also, in the information area, it is necessary to show how much the photo file size has changed relative to the original (in percent and kilobytes). By clicking on the “Next” button, the user goes to the third screen.

The third screen.
Consists of the following components:
1. Preview area, where the photo that was initially selected is displayed

2. Preview area, where the photo that was obtained after changes on the second screen is displayed.

3. Above each preview area, the size in kilobytes should be displayed.
