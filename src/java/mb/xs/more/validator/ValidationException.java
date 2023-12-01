package mb.xs.more.validator;

import mb.xs.core.model.Model;

public class ValidationException extends Exception {
	private static final long serialVersionUID = 2073885027037435441L;

	private final Model<?> type;
	private final Object value;

	public ValidationException( Model<?> type, Object value, String message ) {
		super( message );
		this.type = type;
		this.value = value;
	}
	public ValidationException( ValidationException child, Model<?> type, Object value, String message ) {
		super( message, child );
		this.type = type;
		this.value = value;
	}

	public Model<?> getType() {
		return type;
	}
	public Object getValue() {
		return value;
	}

	@Override
	public String getMessage() {
		return type.getActual().getSimpleName() + ": " + super.getMessage();
	}
}
