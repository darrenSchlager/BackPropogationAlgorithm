/*
	Author: Darren Schlager
*/

import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;;

public class BackPropogationAlgorithm {
	
	// class variables
	static double alpha = 1; 			// used by logsig
	static double beta = 3;				// used by logsig
	static double gamma = 0;			// used by logsig
	static double delta = 0;			// used by logsig
	static double epsilon = 0.1; 		// the sum of squares error must be less than this value
	static double[] x2 = {0,0,0,0};		// input node values
	static double[][] w2 = { 			// weights from each input node to each node in the middle layer
						{0.1,0.1,0.1}, 
						{0.1,0.1,0.1}, 
						{0.1,0.1,0.1}, 
						{0.1,0.1,0.1} 
					 };
	static double[][] w1 = { 			// weights from each node in the middle layer to each output node
					{0.1,0.1}, 
					{0.1,0.1}, 
					{0.1,0.1}  
	 			};
	static double[] t = {0,0};			// output node expected values
	
	public static void main(String[] args) 
	{

		initialize(); // set the class variables using the contents of a file; comment out this line to use the default values
		
		
		// BEGIN Back Propogation Algorithm
		/*
			step 1
		*/
		
		//compute x1
		double[] result = matrixMultiply(x2, w2);
		double[] x1 = new double[result.length];
		for(int i=0; i<result.length; i++)
		{
			x1[i] = logsig(result[i]);
		}
		
		//compute x0
		result = matrixMultiply(x1, w1);
		double[] x0 = new double[result.length];
		for(int i=0; i<result.length; i++)
		{
			x0[i] = logsig(result[i]);
		}
		System.out.print("outputs: ");
		printMatrix(x0);
		double[] error = matrixSubtract(t, x0);
		
		/*
			step 6
		*/
		
		double sum = 0;
		for(int i=0; i<error.length; i++) 
		{
			sum += Math.pow(error[i],2);
		}
		double currentEpsilon = Math.sqrt(sum);
		System.out.print("epsilon: ");
		System.out.println(currentEpsilon+"\n");
		
		/*
			loop
		*/
			
		while(currentEpsilon>=epsilon)
		{
			/*
				step 2
			*/
			
			double[][] d1 = new double[x0.length][x0.length];
			for(int i=0; i<x0.length; i++)
				d1[i][i] = beta*x0[i]*(1-x0[i]);
			double[][] delta0 = matrixMultiply(d1, matrixTranspose(error));
			
			/*
				step 3
			*/
			
			double[][] d2 = new double[x1.length][x1.length];
			for(int i=0; i<x1.length; i++)
				d2[i][i] = beta*x1[i]*(1-x1[i]);
			double[][] delta1 = matrixMultiply(d2, w1);
			delta1 = matrixMultiply(delta1, delta0);
			
			/*
				step 4
			*/
			
			double[][] left = matrixTranspose(w1);
			double[][] right = matrixMultiply(delta0, x1);
			w1 = matrixTranspose(matrixAdd(left, right));
			
			/*
				step 5
			*/
			
			left = matrixTranspose(w2);
			right = matrixMultiply(delta1, x2);
			w2 = matrixTranspose(matrixAdd(left, right));
			
			/*
				step 1
			*/
			
			//compute x1
			result = matrixMultiply(x2, w2);
			for(int i=0; i<result.length; i++)
			{
				x1[i] = logsig(result[i]);
			}
			
			//compute x0
			result = matrixMultiply(x1, w1);
			for(int i=0; i<result.length; i++)
			{
				x0[i] = logsig(result[i]);
			}
			System.out.print("outputs: ");
			printMatrix(x0);
			error = matrixSubtract(t, x0);
			
			/*
				step 6
			*/
			
			sum = 0;
			for(int i=0; i<error.length; i++) 
			{
				sum += Math.pow(error[i],2);
			}
			currentEpsilon = Math.sqrt(sum);
			System.out.print("epsilon: ");
			System.out.println(currentEpsilon+"\n");
		}
		
		System.out.println("------------------------------------------------\n");
		System.out.println("w2");
		printMatrix(w2);
		System.out.println("\nw1");
		printMatrix(w1);
		
		// END Back Propogation Algorithm
	}
	
	
	/*
		sets the class variables using the contents of a file
	*/
	public static void initialize()
	{
		// used to retrieve keyboard input from the user
		Scanner keyboard = new Scanner(System.in);
		
		// print description of expeteted file contents
		System.out.println("BackPropogationAlgorithm\n");
		System.out.println("Format your file as follows:");
		System.out.println("=========================================================================================================");
		System.out.println(" <alpha> <beta> <gamma> <delta> <epsilon>");
		System.out.println(" <number of input nodes> <number of nodes in the middle layer> <number of output nodes> <default weight>");
		System.out.println(" <input 1> <input 2> ... <input n>");
		System.out.println(" <expected output 1> <expected output  2> ... <expected output  n>");
		System.out.println("=========================================================================================================");
		
		Scanner file;
		boolean fileProcessedSuccessfully = false;
		do {
			
			// prompt the user for the file path
			System.out.print("file path: ");
			String path = keyboard.nextLine();
			System.out.println();
			
			try 
			{
				// valid file
				
				// open the file
				file = new Scanner(new FileReader(path));
				
				try 
				{
					alpha = file.nextDouble();
					
					try 
					{
						beta = file.nextDouble();
						
						try 
						{
							gamma = file.nextDouble();
							
							try 
							{
								delta = file.nextDouble();
								
								try 
								{
									epsilon = file.nextDouble();
									
									//get the number of input nodes
									int numInputs = 0;
									try 
									{
										numInputs = file.nextInt();
										
										// get the number of nodes in the middle layer
										int numMiddleLayer = 0;
										try 
										{
											numMiddleLayer = file.nextInt();
											
											//get the number of output nodes
											int numOutputs = 0;
											try 
											{
												numOutputs = file.nextInt();
												
												// get the default weight
												double defaultWeight = 0;
												try 
												{
													defaultWeight = file.nextDouble();
													
													// set the class variable to contain the value for each input node
													x2 = new double[numInputs];
													try 
													{
														for(int i=0; i<numInputs; i++)
														{
															x2[i] = file.nextDouble();
														}
														
														// set the class variable to contain the expected value for each output node
														t = new double[numOutputs];
														try 
														{
															for(int i=0; i<numOutputs; i++)
															{
																t[i] = file.nextDouble();
															}
															
															// set the class variable to contain the weights from each input node to each node in the middle layer
															w2 = new double[numInputs][numMiddleLayer];
															for(int i=0; i<numInputs; i++)
															{
																for(int j=0; j<numMiddleLayer; j++)
																{
																	w2[i][j] = defaultWeight;
																}
															}
															
															// set the class variable to contain the weights from each node in the middle layer to each output node
															w1 = new double[numMiddleLayer][numOutputs];
															for(int i=0; i<numMiddleLayer; i++)
															{
																for(int j=0; j<numOutputs; j++)
																{
																	w1[i][j] = defaultWeight;
																}
															}
															
															fileProcessedSuccessfully = true;
														}
														catch (Exception e) // t
														{
															System.out.println("That file is not formatted correctly. Try Again.");
														}
													}
													catch (Exception e) // x2
													{
														System.out.println("That file is not formatted correctly. Try Again.");
													}
												}
												catch (Exception e) //defaultWeight
												{
													System.out.println("That file is not formatted correctly. Try Again.");
												}
											}
											catch (Exception e) //numOutputs
											{
												System.out.println("That file is not formatted correctly. Try Again.");
											}
										}
										catch (Exception e) // numMiddleLayer
										{
											System.out.println("That file is not formatted correctly. Try Again.");
										}
									}
									catch (Exception e) // numInputs
									{
										System.out.println("That file is not formatted correctly. Try Again.");
									}
								}
								catch (Exception e) // epsilon
								{
									System.out.println("That file is not formatted correctly. Try Again.");
								}
							}
							catch (Exception e) // delta
							{
								System.out.println("That file is not formatted correctly. Try Again.");
							}
						}
						catch (Exception e) // gamma
						{
							System.out.println("That file is not formatted correctly. Try Again.");
						}
					}
					catch (Exception e) // beta
					{
						System.out.println("That file is not formatted correctly. Try Again.");
					}
				}
				catch (Exception e) // alpha
				{
					System.out.println("That file is not formatted correctly. Try Again.");
				}
				
				//close the file
				file.close();
			} 
			catch (IOException e) // file
			{
				// invalid file
				System.out.println("That file does not exist. Try Again.");
			}
			
		} while (!fileProcessedSuccessfully);
	}
	
	/*
		compute logsig
	*/	
	public static double logsig(double x) 
	{
		return ( alpha / (1+Math.pow( Math.E, -1*beta*(x-gamma))) ) + delta;
	}
	
	/*
		print the contents of a Nx1 matrix
	*/
	public static void printMatrix(double[] matrix)
	{
		for (int i=0; i<matrix.length; i++) 
		{
			System.out.print(matrix[i]+" ");
		}
		System.out.println();
	}
	
	/*
		print the contents of a NxN matrix	
	*/
	public static void printMatrix(double[][] matrix) {
		for(int i=0; i<matrix.length; i++) 
		{
			for (int j=0; j<matrix[i].length; j++) 
			{
				System.out.print(matrix[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	/*
		the row in a Nx1 matrix becomes the column in a 1xN matrix	
	*/
	public static double[][] matrixTranspose(double[] arr)
	{
		double[][] result = new double[arr.length][1];
		for(int i=0; i<arr.length; i++)
		{
			result[i][0] = arr[i];
		}
		return result;
	}
	
	/*
		the rows in a NxN matrix become the columns in a NxN matrix
	*/
	public static double[][] matrixTranspose(double[][] arr)
	{
		double[][] result = new double[arr[0].length][arr.length];
		for(int i=0; i<arr.length; i++)
		{
			for(int j=0; j<arr[i].length; j++)
			result[j][i] = arr[i][j];
		}
		return result;
	}
	
	/*
		matrix subtraction (1xN)
			- perform subtraction on each element with the same index
			- the matrices must have the same dimensions 
	*/
	public static double[] matrixSubtract(double[] arr1, double arr2[]) 
	{
		if(arr1.length==arr2.length)
		{
			double[] result = new double[arr1.length];
			for(int i=0; i<arr1.length; i++) 
			{
				result[i] = arr1[i]-arr2[i];
			}
			return result;
		}
		else
		{
			return null;	
		}
	}
	
	/*
		matrix addition (NxN)
			- perform subtraction on each element with the same index
			- the matrices must have the same dimensions
	*/
	public static double[][] matrixAdd(double[][] arr1, double arr2[][]) 
	{
		if(arr1.length==arr2.length && arr1[0].length==arr2[0].length)
		{
			double[][] result = new double[arr1.length][arr1[0].length];
			for(int i=0; i<arr1.length; i++) 
			{
				for(int j=0; j<arr1[0].length; j++)
					result[i][j] = arr1[i][j]+arr2[i][j];
			}
			return result;
		}
		else
		{
			return null;	
		}
	}
	
	/*
		matrix multiplication (NxM, MxP)
			- for each row in the first matrix, repeat the following for each column in the second matrix
				+ multiply each element in the row by the corresponding element in the column; sum the results
			- the number of columns in the first matrix MUST equal the number of rows in the second matrix
				 
	*/
	public static double[][] matrixMultiply(double[][] arr1, double arr2[][]) 
	{
		if(arr1[0].length==arr2.length) 
		{
			
			int numRow = arr1.length;
			int numCol = arr2[0].length;
			double[][] result = new double[numRow][numCol];
			
			for(int i=0; i<numRow; i++) 
			{
				for(int j=0; j<numCol; j++)
				{
					double sum = 0;
					for(int k=0; k<arr1[i].length; k++)
						sum += arr1[i][k]*arr2[k][j];
					result[i][j] = sum;
				}
			}
			
			return result;
		}
		else 
		{
			return null;
		}
	}
	
	/*
		matrix multiplication (1xM, MxP)
			- for each column in the second matrix
				+ multiply each element in the row of the first matrix by the corresponding element in the column; sum the results
			- the number of columns in the first matrix MUST equal the number of rows in the second matrix
				 
	*/
	public static double[] matrixMultiply(double[] arr1, double arr2[][]) 
	{
		if(arr1.length==arr2.length) 
		{
			int numCol = arr2[0].length;
			double[] result = new double[numCol];
			
			for(int i=0; i<numCol; i++)
			{
				double sum = 0;
				for(int j=0; j<arr1.length; j++) 
					sum += arr1[j]*arr2[j][i];
				result[i] = sum;
			}
			
			return result;
		}
		else 
		{
			return null;
		}
	}
	
	
	/*
		matrix multiplication (NxM, Mx1)
			- for each row in the first matrix
				+ multiply each element in the row by the corresponding element in the column of the second matrix; sum the results
			- the number of columns in the first matrix MUST equal the number of rows in the second matrix
				 
	*/
	public static double[][] matrixMultiply(double[][] arr1, double arr2[]) 
	{
		if(arr1[0].length==1) 
		{
			int numRow = arr1.length;
			int numCol = arr2.length;
			double[][] result = new double[numRow][numCol];
			
			for(int i=0; i<numRow; i++) 
			{
				for(int j=0; j<numCol; j++)
					result[i][j] = arr1[i][0]*arr2[j];
			}
			
			return result;
		}
		else 
		{
			return null;
		}
	}
	
}