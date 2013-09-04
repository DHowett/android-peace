package net.howett.android.peace;

import com.saurik.substrate.MS;
import java.lang.reflect.*;
import android.util.Log;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;

public class Main {
	static void nop(final String classname, final String method, final Object retval, final Class<?>... args) {
		MS.hookClassLoad(classname, new MS.ClassLoadHook() {
			public void classLoaded(Class<?> _class) {
				try {
					MS.hookMethod(_class, _class.getDeclaredMethod(method, (Class<?>[])args), new MS.MethodAlteration() {
						public Object invoked(Object resources, Object... args) throws Throwable {
							return retval;
						}
					});
				} catch (Exception e) { Log.w("SENSELESS", "Failed to collect method.", e); }
			}
		});
	}
	static void initialize() {
		nop("android.net.wifi.WifiStateMachine", "sendVzwStatusNotification", 0, Integer.TYPE);
		nop("com.android.server.usb.UsbDeviceManager$UsbHandler", "updateAdbNotification", null, (Class<?>[])null);

		nop("com.android.nfc.NfcService", "getNfcIconId", 0x7f020004, (Class<?>[])null);

		MS.hookClassLoad("com.android.systemui.statusbar.policy.HtcTelephonyIcons", new MS.ClassLoadHook() {
			public void classLoaded(final Class<?> _class) {
				Field v3g, h3g, vlte, hlte;
				try {
					v3g = _class.getDeclaredField("VZW_DATA_3G");
					h3g = _class.getDeclaredField("HTC_DATA_3G");
					vlte = _class.getDeclaredField("VZW_DATA_LTE");
					hlte = _class.getDeclaredField("HTC_DATA_LTE");
					v3g.setAccessible(true);
					vlte.setAccessible(true);
					v3g.set(null, h3g.get(null));
					vlte.set(null, hlte.get(null));
				} catch(Exception e) {
					Log.e("SENSELESS", "Failed to replace 3G/LTE icons.", e);
				}
			}
		});
	}
}
