package teste;

import java.io.*;

public class NetworkUsers
    {
     public static void main(java.lang.String[] args)
         {
         StringBuffer strBuffer= new StringBuffer();
         String line= null;
         try
             {
             Process process= Runtime.getRuntime().exec("cmd");
             BufferedReader b = new BufferedReader(new InputStreamReader(process.getInputStream()));
             PrintWriter to= new PrintWriter(process.getOutputStream());
             to.println("net view");
             to.println("exit");
             to.close();
            
             try
                 {
                 // we need the process to end, else we'll get an
                 // illegal Thread State Exception
                 line= b.readLine();
                 while (line != null)
                     {
                     strBuffer.append(line+"\n");
                     line= b.readLine();
                 }
                 process.waitFor();
             }
             catch (InterruptedException inte)
                 {
                 System.out.println("InterruptedException Caught");
             }
            
             if (process.exitValue() == 0)
                 {
                 System.out.println(strBuffer.toString());
             }
            
             process.destroy();
            
         }
         catch (IOException ioe)
             {
             System.out.println(
             "IO Exception Occured While Messing aruond with Processes! -> " + ioe);
         }
     }
}
