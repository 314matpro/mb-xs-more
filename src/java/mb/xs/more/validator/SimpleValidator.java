package mb.xs.more.validator;

import java.util.Objects;

import mb.xs.core.model.*;

public class SimpleValidator<T> implements Validator<T> {
	private Model<T> type;
	private T value;

	public SimpleValidator( Class<T> type, T value ) {
		this( new ModelType<>( type ), value );
	}
	public SimpleValidator( Model<T> type, T value ) {
		this.type = type;
		this.value = value;
	}

	@Override
	public Model<T> getType() {
		return type;
	}
	@Override
	public T getValue() {
		return value;
	}

	@Override
	public SimpleValidator<T> isNull() throws ValidationException {
		if( value != null ) {
			throw new ValidationException( type, value, "should be null" );
		}
		return this;
	}
	@Override
	public SimpleValidator<T> isNotNull() throws ValidationException {
		if( value == null ) {
			throw new ValidationException( type, value, "should NOT be null" );
		}
		return this;
	}
	@Override
	public SimpleValidator<T> isEqual( T other ) throws ValidationException {
		if( !Objects.equals( value, other ) ) {
			throw new ValidationException( type, value, "should be equal to " + other );
		}
		return this;
	}
	@Override
	public SimpleValidator<T> isNotEqual( T other ) throws ValidationException {
		if( Objects.equals( value, other ) ) {
			throw new ValidationException( type, value, "should NOT be equal to " + other );
		}
		return this;
	}
}
