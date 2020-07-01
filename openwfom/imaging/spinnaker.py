import numpy as np
import queue, threading, time

try:
    import PySpin
except ModuleNotFoundError:
    print("\nPySpin is not installed")
    print("\nFollow the directions here to install it:")
    print("\nhttps://github.com/ryan-byrne/openwfom/wiki/Camera-Setup/#installing-the-spinnaker-sdk\n")
    raise

class FlirError(Exception):
    pass

class Capture(object):
    """docstring for Flir."""

    def __init__(self):

        print("Initializing FLIR Cameras...")

        self.active = False

        self.frames = [np.zeros((500,500), 'uint8')]

        self.system = PySpin.System.GetInstance()
        self.cameras = self.system.GetCameras()

        if self.cameras.GetSize() == 0:
            self.shutdown()
            msg = "There are no FLIR Cameras attached."
            raise ConnectionError(msg)

        print("There are {0} FLIR Camera(s) attached".format(self.cameras.GetSize()))
        for cam in self.cameras:
            print("SN: {0}".format(self.get_serial_number(cam)))

        threading.Thread(target=self._update_frames).start()

    def get_serial_number(self, cam):
        return PySpin.CStringPtr(cam.GetTLDeviceNodeMap().GetNode('DeviceSerialNumber')).GetValue()

    def _read_image(self, cam):
        try:
            image_result = cam.GetNextImage(1000)
            img = np.reshape(   image_result.GetData(),
                                (image_result.GetHeight(),image_result.GetWidth())
                            )
            image_result.Release()
            return img
        except PySpin.SpinnakerException as e:
            print("{0}: {1}".format(self.get_serial_number(cam), e))

    def _update_frames(self):

        for i, cam in enumerate(self.cameras):
            self.active = self._start_camera(cam)

        print("Updating Flir camera frames...")

        while self.active:
            imgs = []
            for cam in self.cameras:
                imgs.append(self._read_image(cam))
            self.frames = imgs

        for cam in self.cameras:
            self._stop_camera(cam)

    def _start_camera(self, cam):

        print("Initialising FLIR Camera, SN: {0}...".format(self.get_serial_number(cam)))

        try:
            cam.Init()
            time.sleep(1)
            cam.BeginAcquisition()
            return True
        except:
            try:
                print("SN: {0} was already initialized. Restarting...".format(self.get_serial_number(cam)))
                cam.DeInit()
                cam.Init()
                cam.BeginAcquisition()
            except Exception as e:
                print("Unable to Start Camera, SN: {0}...".format(self.get_serial_number(cam)))
                print(e)
                return False

    def _stop_camera(self, cam):

        print("Stopping FLIR Camera, SN: {0}...".format(self.get_serial_number(cam)))

        try:
            cam.EndAcquisition()
            cam.DeInit()
        except PySpin.SpinnakerException as e:
            print("Unable to Stop Camera, SN: {0}...".format(self.get_serial_number(cam)))
            print(e)

        del cam

    def shutdown(self):

        print("Closing FLIR Cameras...")

        self.active = False
        time.sleep(1)

        print("Clearing Camera list...")
        self.cameras.Clear()

        print("Releasing Spinnaker SDK...")
        self.system.ReleaseInstance()

if __name__ == '__main__':
    import gui
    flir = Camera(True)
    gui = gui.Frame("Flir Test")
    while True:
        if not gui.view(flir.frames[0], flir.frames):
            break
    flir.close()