package mb.xs.more.validator;

import java.util.function.Predicate;

import mb.xs.core.model.Model;

public interface Validator<T> {
	public Model<T> getType();
	public T getValue();

	public Validator<T> isNotNull() throws ValidationException;
	public Validator<T> isNull() throws ValidationException;
	public Validator<T> isEqual( T other ) throws ValidationException;
	public Validator<T> isNotEqual( T other ) throws ValidationException;
	public Validator<T> is( Predicate<T> condition ) throws ValidationException;
}
