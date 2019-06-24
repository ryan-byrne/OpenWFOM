import time, subprocess, os, datetime, json, sys, string
from arduino import Arduino
from solis import Solis
from gui import Gui


if __name__ == '__main__':

    # Welcome Banner
    Gui.banner("WFOM", "isometric1")
    Gui.banner("WELCOME TO SPLASSH", "contessa")
    Arduino.message_to_arduino("s")
    # Initiate the SPLASSH Gui
    uni, mouse = Gui.open_GUI()
    try:
        path = Solis.create_camera_file_folder(mouse)
    except FileExistsError as e:
        print(e.with_traceback)
        Gui.exit()
    Gui.solis_GUI()
    try:
        dst = Solis.deploy_settings(path)
        msg = Arduino.get_message(dst)
        Arduino.message_to_arduino(msg)
    except FileNotFoundError as e:
        print(e.filename)
