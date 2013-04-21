#!/bin/bash
#This has to run as root
#Place in crontab at 10 ms

fswebcam -d /dev/video0 /var/www/stereobalance/images/left.jpg # Fetch image from left camera
fswebcam -d /dev/video1 /var/www/stereobalance/images/right.jpg #Fetch image from right camera
