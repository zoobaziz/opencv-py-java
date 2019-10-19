import cv2
import numpy as np
import os


def videoToFrame():
    # set video file path of input video with name and extension

    vid1 = cv2.VideoCapture('./Video/Startup.mp4')

    index = 2
    while True:
        ret, frame = vid1.read()
        if not ret:
            break
        name1 = 'D:\\Frames\\Video1\\Frame_' + str(index) + '.png'
        print('Creating...' + name1)

        cv2.imwrite(name1, frame)

        index += 1


videoToFrame()
