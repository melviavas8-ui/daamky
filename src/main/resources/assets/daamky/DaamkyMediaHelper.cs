using System;
using System.IO;
using Windows.Media.Control;

class Program
{
    [System.Runtime.InteropServices.DllImport("user32.dll")]
    static extern void keybd_event(byte bVk, byte bScan, uint dwFlags, int dwExtraInfo);

    const byte VK_MEDIA_NEXT_TRACK = 0xB0;
    const byte VK_MEDIA_PREV_TRACK = 0xB1;
    const byte VK_MEDIA_PLAY_PAUSE = 0xB3;
    const uint KEYEVENTF_KEYUP = 0x0002;

    static string lastTitle = null;
    static string lastArtist = null;
    static string lastImage = null;
    static bool lastHasSession = false;

    static void Main(string[] args)
    {
        Console.OutputEncoding = System.Text.Encoding.UTF8;

        // Start standard input reader thread
        var inputThread = new System.Threading.Thread(ReadInputLoop);
        inputThread.IsBackground = true;
        inputThread.Start();

        while (true)
        {
            try
            {
                var op = GlobalSystemMediaTransportControlsSessionManager.RequestAsync();
                var manager = System.WindowsRuntimeSystemExtensions.AsTask(op).Result;
                
                var session = manager.GetCurrentSession();
                if (session != null)
                {
                    var op2 = session.TryGetMediaPropertiesAsync();
                    var props = System.WindowsRuntimeSystemExtensions.AsTask(op2).Result;
                    if (props != null)
                    {
                        string title = props.Title ?? "";
                        string artist = props.Artist ?? "";
                        string base64 = "";

                        if (props.Thumbnail != null)
                        {
                            try
                            {
                                var op3 = props.Thumbnail.OpenReadAsync();
                                var stream = System.WindowsRuntimeSystemExtensions.AsTask(op3).Result;
                                using (var netStream = System.IO.WindowsRuntimeStreamExtensions.AsStreamForRead(stream))
                                using (var ms = new MemoryStream())
                                {
                                    netStream.CopyTo(ms);
                                    base64 = Convert.ToBase64String(ms.ToArray());
                                }
                            }
                            catch {}
                        }

                        if (!lastHasSession || title != lastTitle || artist != lastArtist || base64 != lastImage)
                        {
                            Console.WriteLine("TITLE:" + title);
                            Console.WriteLine("ARTIST:" + artist);
                            Console.WriteLine("IMAGE:" + base64);
                            Console.WriteLine("---");
                            
                            lastTitle = title;
                            lastArtist = artist;
                            lastImage = base64;
                            lastHasSession = true;
                        }
                    }
                }
                else
                {
                    if (lastHasSession)
                    {
                        Console.WriteLine("NONE");
                        Console.WriteLine("---");
                        lastHasSession = false;
                        lastTitle = null;
                        lastArtist = null;
                        lastImage = null;
                    }
                }
            }
            catch {}

            System.Threading.Thread.Sleep(1000);
        }
    }

    static void SendMediaKey(byte key)
    {
        keybd_event(key, 0, 0, 0);
        keybd_event(key, 0, KEYEVENTF_KEYUP, 0);
    }

    static void ReadInputLoop()
    {
        string line;
        while ((line = Console.ReadLine()) != null)
        {
            try
            {
                var op = GlobalSystemMediaTransportControlsSessionManager.RequestAsync();
                var manager = System.WindowsRuntimeSystemExtensions.AsTask(op).Result;
                var session = manager.GetCurrentSession();
                
                bool handledByGSMTC = false;
                if (session != null)
                {
                    try
                    {
                        if (line == "PLAYPAUSE")
                        {
                            var op2 = session.TryTogglePlayPauseAsync();
                            var res = System.WindowsRuntimeSystemExtensions.AsTask(op2).Result;
                            handledByGSMTC = res;
                        }
                        else if (line == "NEXT")
                        {
                            var op2 = session.TrySkipNextAsync();
                            var res = System.WindowsRuntimeSystemExtensions.AsTask(op2).Result;
                            handledByGSMTC = res;
                        }
                        else if (line == "PREV")
                        {
                            var op2 = session.TrySkipPreviousAsync();
                            var res = System.WindowsRuntimeSystemExtensions.AsTask(op2).Result;
                            handledByGSMTC = res;
                        }
                    }
                    catch {}
                }

                if (!handledByGSMTC)
                {
                    if (line == "PLAYPAUSE")
                    {
                        SendMediaKey(VK_MEDIA_PLAY_PAUSE);
                    }
                    else if (line == "NEXT")
                    {
                        SendMediaKey(VK_MEDIA_NEXT_TRACK);
                    }
                    else if (line == "PREV")
                    {
                        SendMediaKey(VK_MEDIA_PREV_TRACK);
                    }
                }
            }
            catch {}
        }
    }
}
