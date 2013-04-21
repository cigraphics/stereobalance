#!/bin/bash
## This script sends commands to Arduino

## Initialize output pins
## Forward/backwards
gpio mode 4 out;
gpio mode 5 out;
## Turn
gpio mode 3 out;
gpio mode 2 out;
## Write 0 to everything to ensure a clean start
input=0;
gpio write 4 0;
gpio write 5 0;
gpio write 3 0;
gpio write 2 0;
## Main loop, which normally never stops
while :
	do
		input=$(cat direction.txt);
		case "$input" in:
			1) gpio write 4 1;  ## Phone tilted forward -> go forward
			   gpio write 5 0;;
			0) gpio write 4 0;  ## Phone horizontal -> stop
			   gpio write 5 0;;
			-1) gpio write 4 0; ## Phone tilted backwards -> go backwards
			    gpio write 5 1;;
			*) ## When functioning in normal parameters, the loop should never get here
			   echo "Wrong input for direction, command reading process might have failed.";
			   echo "Exiting";
			   gpio write 4 0;
			   gpio write 5 0;
			   gpio write 2 0;
			   gpio write 3 0;
			   exit;;
		esac;
		input=$(cat turn.txt);
		case "$input" in:
			r) gpio write 3 1;  ## Turn right
			   gpio write 2 0;;
			l) gpio write 2 1;  ## Turn left
			   gpio write 3 0;;
			0) gpio write 3 0;
			   gpio write 2 0;; ## No turn
			*) ## When functioning in normal parameters, the loop should never get here
			   echo "Wrong input for turn, command reading process might have failed.";
			   echo "Exiting";
			   gpio write 4 0;
			   gpio write 5 0;
			   gpio write 2 0;
			   gpio write 3 0;
			   exit;;
		esac;
done
