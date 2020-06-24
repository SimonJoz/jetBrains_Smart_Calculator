# Smart Calculator 


The program calculates the result, of mathematical operations such as:
 - ' + ' : Addition.
 - ' - ' : Subtraction.
 - ' * ' : Multiplication.
 - ' / ' : Integer division.
 - ' ^ ' : Raising to a power.

Calculator also supports variables, very large numbers and parentheses (...).

Application use postfix notation, also known as reverse Polish notation (RPN).

# Description and example of usage:
 - "/help" : Print the usage description.
 - "/exit" - Quit the program.
 - To print the value of existing variable simply type its name.
 - Assign value to variable:
 
        <variable> = <number> 
        <variable> = <existing_variable>
        e.g count = 10, a = 4, b = a, c = b;
 
 - Set new value to existing variable:
  
         <existingVariable> = <newValue>
         e.g a = 1, b = 2, a = b, a = 2;
         a = 800000000000000000000000;
 
 - Invalid assignment :
 
         Assigning value must be a number or existing variable 
         
                    <variableName> = 5d
                    <variableName> = dd5
                    <variableName> = ++6d
                    <variableName> = 6 = 7
   
  - Invalid identifier :
  
         A variable identifier must not contain any non letter characters. 
         
                    - a9( = <value>
                    - 3d  = <value>
                    - a5  = <existing_variable>
                    - ++  = <existing_variable>
        
 - Invalid expression :
        
         Missing operator :        3 + 5 (4 -2)
         Missing closing parenthesis :  4 * (2 + 3  
         Missing opening parenthesis :  4 + 3)      
         Invalid use of operator : 3 /// 4
         Invalid use of operator : 3 *** 5 
         
          

 - Correct expression:
       
       - Please note that even number of minuses gives plus, odd gives minus:
                   
                   e.g '2 -- 2' ==> '2 - (-2)' ==> '2 + 2'.
                   
       - Multiple usage of plus operator is valid.  
       - Spaces order does not effect calculation.           
       - Valid expressions e.g :
                - 3 -- 3 --- 2 ++ 6 
                - a- b  + 4 -c*3- (g+4  )
                - 1 +++ 2 * 3 - 4
                - a*2+b*3+c*(2+3)
                - 112234567890 + 112234567890 * (10000000999 - 999).
                -  3 + 8 * ((4 + 3) * 2 + 1) - 6 / (2 + 1)
                -  2 * 2^3
                -  var  
                -  3 + 8 / ((4 + 3) * 2^2)
       
                        