package org.spout.vanilla.util;

public interface ShortOperation {

	/**
	 * Executes this operation
	 * 
	 * @param input value to operate on
	 * @return the result of the operation
	 */
	public short getResult(short input);


	public short convert(short input, short parameter);
	
}
