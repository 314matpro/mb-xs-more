package mb.xs.more.validator;

import java.util.Collection;

import mb.xs.core.ExceptionalHandler;
import mb.xs.core.model.*;

public class CollectionValidator<T> extends SimpleValidator<Collection<T>> {
	private Model<T> itemType;

	public CollectionValidator( Class<T> itemType, Collection<T> value ) {
		this( new ModelType<>( itemType ), value );
	}
	public CollectionValidator( Model<T> itemType, Collection<T> value ) {
		super( Types.createModelCollection( itemType ), value );
	}

	public Model<T> getItemType() {
		return itemType;
	}

	@Override
	public CollectionValidator<T> isNull() throws ValidationException {
		super.isNull();
		return this;
	}
	@Override
	public CollectionValidator<T> isNotNull() throws ValidationException {
		super.isNotNull();
		return this;
	}
	@Override
	public CollectionValidator<T> isEqual( Collection<T> other ) throws ValidationException {
		super.isEqual( other );
		return this;
	}
	@Override
	public CollectionValidator<T> isNotEqual( Collection<T> other ) throws ValidationException {
		super.isNotEqual( other );
		return this;
	}

	public CollectionValidator<T> contains( String name, //
			ExceptionalHandler<ObjectValidator<T>, ValidationException> validator ) throws ValidationException {
		isNotNull();
		for( T item : getValue() ) {
			try {
				validator.handle( new ObjectValidator<>( itemType, item ) );
				return this;
			} catch( ValidationException e ) {
			}
		}
		throw new ValidationException( getType(), getValue(), "Nothing contains " + name );
	}
}
