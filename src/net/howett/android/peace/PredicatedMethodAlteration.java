package net.howett.android.peace;

import com.saurik.substrate.MS;

public class PredicatedMethodAlteration extends MS.MethodAlteration {
	private Predicate _predicate;
	private Object _return;

	public PredicatedMethodAlteration(Object falseReturn, Predicate predicate) {
		_predicate = predicate;
		_return = falseReturn;
	}

	public Object invoked(Object object, Object... args) throws Throwable {
		if(_predicate.test(object, args)) {
			return invoke(object, args);
		}
		return _return;
	}
}
