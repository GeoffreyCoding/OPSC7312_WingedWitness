Team:
//-----------------------------------------------
OPSC7312_POE_PART2
Geoffrey Huth - ST10081932
Gabriel Grobbelaar - ST10082002
Liam Colbert - ST10081986
//-----------------------------------------------
App Name:
WingedWitnesses

System Setting:
- Android stuio electric eel
- We were running on pixel 6 pro api 30

NB: Due to the limitations on the emulator and how it only exists in software, it is recommended to run the app on a physical android device, however, if required to run on an emulator the position of the user will not change because the emulator requires a set gps location that cannot change.

Required to run in emulator:

The emulator must have the google plays store installed, otherwise it will only work on a physical android device.

Steps to install google playstore:
1. Close Android studio
2. Open file explorer
3. Navigate to "C:\Users\Geoffrey\.android\avd\Pixel_6_Pro_API_30.avd"
3. Make a copy of the config file in case of emergency
4. Open the original config file
5. On line 2 change PlayStore.enabled = false to PlayStore.enabled = true
6. Look for this line "image.sysdir.1=system-images\android-30\google_apis_playstore\x86\" and add _playstore to the end
		- The line should now look like "image.sysdir.1=system-images\android-30\google_apis_playstore\x86\_playstore"
7. Save the changes
8. run the app

How to use:
1. click on sign-up and then enter the following details
	- Email ( must be a valid email)
	- Password ( Must be longer than 10)
	- Confirm password (Must match password)

2. Enter the details in login then click login

3. You can now click on any of the buttons which will take you to that activity or fragment
__________________________________________________________________________________________________________________________________________

4. Hotspots

- Once you click on the hotspots button, the map will be displayed.
- Click te location icon on the right then the hotspots will be displayed
- You can change the display radius of the hotspots using the spinner in the top left
- To navigate, click on the hotspot and now you will see all the bird sightings at the hotspot and navigate to the hotspot

5. Add Sighting

- Once you click on the Add Sighting buttont a new activity will be displayed. 
- Enter all the details.
- The Species can be added by clicking the Add button and then selecting a species.
- You can then either take a photo or record the audio of the bird
- Finally, once all details are entered you can click the add sighting button at the bottom.

6: Account
- Once you click account you will be able to view all the sightings you have added or change the distance configuration to either imperial or the metric systems respectfully.
- On each diary entry you can see the picture taken, play the audio, or click the "i" info button to load the wikipedia page of the species.

7: Quiz Game
- From the homepage, once you click the quiz game it will pre-populate the first set of questions.
- Once youve answered all the questions click check answers 
- if your happy with your answers click next questions.
- Keep proceeding till all the questions are finished

8: Loggout
- simply takes you back to the login page.
