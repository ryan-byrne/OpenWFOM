import time, argparse, sys, os, json, pkgutil
import numpy as np
import tkinter as tk

def _get_args():

    """
    This function simply checks for arguments when the script is

    and stores them in their corresponding variable.

    Example:

    wfom 0

    """

    parser = argparse.ArgumentParser(description="Command line tool for the pywfom library.")

    msg = "Run a diagnostic test of your pywfom installation."
    parser.add_argument('-t', '--test', dest='test', action='store_true', default=False, help=msg)

    msg = "Print additional text while running pywfom."
    parser.add_argument('-v', '--verbose', dest='verbose', action='store_true', default=False, help=msg)

    msg = "Option to run pywfom with Solis' built-in User Interface."
    parser.add_argument('-s', '--solis', dest='solis', action='store_true', default=False, help=msg)

    msg = "Use previously saved configuration (.json) file"
    parser.add_argument(    '-c',
                            '--config',
                            dest='config',
                            type=str,
                            nargs='?',
                            default="",
                            help=msg
                            )
    args = vars(parser.parse_args())

    return args

def main(config=None):

    from pywfom.imaging.test import TestCamera
    from pywfom.viewing.frame import Frame
    from pywfom.control.arduino import Arduino
    from pywfom.imaging import andor, spinnaker, usb

    CAMERA_TYPES = {
        "andor":andor.Camera,
        "test":TestCamera,
        "spinnaker":spinnaker.Camera,
        "webcam":webcam.Camera
    }

    cameras = [CAMERA_TYPES[cam['type']](cam) for cam in config["cameras"]]
    arduino = Arduino(config["arduino"])

    root = tk.Tk()
    frame = Frame(root, "pywfom", cameras, arduino)
    frame.root.mainloop()

    for cam in cameras:
        cam.close()

def test(config=None):

    from pywfom.imaging import Camera
    from pywfom.viewing import Frame
    from pywfom.control import Arduino
    from pywfom.file import Writer

    cameras = [Camera(config=cfg) for cfg in config["cameras"]]
    arduino = Arduino(config["arduino"])
    file = Writer(config["file"])

    root = tk.Tk()
    frame = Frame(root, "pywfom", cameras, arduino, file)
    frame.root.mainloop()

    for cam in cameras:
        cam.close()


def configure():
    pass

def run():

    # Get command line options
    args = _get_args()

    # Block prints if not verbose
    if not args['verbose']:
        sys.stdout = open(os.devnull, 'w')

    # Load Configuration
    path = args['config'] if args['config'] else "config.json"
    with open(path) as f:
        config = json.load(f)
    f.close()

    # Run test mode or main
    if args["test"]:
        test(config)
    else:
        main(config)

if __name__ == '__main__':
    run()