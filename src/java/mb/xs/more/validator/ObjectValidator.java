package mb.xs.more.validator;

import java.util.Collection;
import java.util.function.Function;

import mb.xs.core.ExceptionalHandler;
import mb.xs.core.model.*;

public class ObjectValidator<T> extends SimpleValidator<T> {
	public ObjectValidator( Class<T> type, T value ) {
		super( type, value );
	}
	public ObjectValidator( Model<T> type, T value ) {
		super( type, value );
	}

	@Override
	public ObjectValidator<T> isNull() throws ValidationException {
		super.isNull();
		return this;
	}
	@Override
	public ObjectValidator<T> isNotNull() throws ValidationException {
		super.isNotNull();
		return this;
	}
	@Override
	public ObjectValidator<T> isEqual( T other ) throws ValidationException {
		super.isEqual( other );
		return this;
	}
	@Override
	public ObjectValidator<T> isNotEqual( T other ) throws ValidationException {
		super.isNotEqual( other );
		return this;
	}

	public <I> ObjectValidator<T> attribute( //
			String name, Class<I> attributeType, Function<T, I> getter, //
			ExceptionalHandler<ObjectValidator<I>, ValidationException> validator ) throws ValidationException {
		return attribute( name, new ModelType<>( attributeType ), getter, validator );
	}
	public <I> ObjectValidator<T> attribute( //
			String name, Model<I> attributeType, Function<T, I> getter, //
			ExceptionalHandler<ObjectValidator<I>, ValidationException> validator ) throws ValidationException {
		isNotNull();
		try {
			validator.handle( new ObjectValidator<>( attributeType, getter.apply( getValue() ) ) );
		} catch( ValidationException e ) {
			throw new ValidationException( e, getType(), getValue(), "Attribute: " + name );
		}
		return this;
	}

	public <I> ObjectValidator<T> attributeCollection( //
			String name, Model<I> collectionType, Function<T, ? extends Collection<I>> getter, //
			ExceptionalHandler<CollectionValidator<I>, ValidationException> validator ) throws ValidationException {
		isNotNull();
		try {
			validator.handle( new CollectionValidator<>( collectionType, getter.apply( getValue() ) ) );
		} catch( ValidationException e ) {
			throw new ValidationException( e, getType(), getValue(), "Attribute: " + name );
		}
		return this;
	}
}
