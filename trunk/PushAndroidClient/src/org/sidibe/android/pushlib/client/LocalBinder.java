package org.sidibe.android.pushlib.client;

import java.lang.ref.WeakReference;

import android.app.Service;
import android.os.Binder;


public class LocalBinder<S extends Service> extends Binder
{
	private WeakReference<S> mService;

	public LocalBinder(S service)
	{
		mService = new WeakReference<S>(service);
	}
	public S getService()
	{
		return mService.get();
	}
	public void close()
	{
		mService = null;
	}
}