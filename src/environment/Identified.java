package environment;

import exceptions.DuplicateIdentifierException;
import exceptions.NoIdentifierException;

public interface Identified {
	String getIdentifier() throws NoIdentifierException;

	void setIdentifier(String id) throws DuplicateIdentifierException;
}
