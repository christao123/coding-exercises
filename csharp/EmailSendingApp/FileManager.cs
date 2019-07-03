using System;
using System.IO;

/// <summary>
/// Manager for file writing and reading
/// </summary>
/// 

namespace WebApplication_
{
    public class FileManager
    {

        /// <summary>
        /// Write string to file
        /// </summary>
        /// <param name="path">path of the file</param>
        /// <param name="content">content of the file </param>
        /// <returns></returns>
        public void WriteToFile(string path, string content)
        {
            try
            {
                File.WriteAllText(path, content);
            }
            catch (Exception e)
            {
                Console.Write("Could not write to file with path {0}\n{1}",path, e.Source);
            }
        }

        /// <summary>
        /// Get The entire content of the file as a string
        /// </summary>
        /// <param name="path">the path of the file to read</param>
        /// <returns></returns>
        public string ReadFromFile(string path)
        {
            try
            {
                return File.ReadAllText(path);
            }
            catch (Exception e)
            {
                Console.Write("Could not read from file with path {0}\n{1}", path, e.Source);
            }

            return null;
        }

        /// <summary>
        /// Get the n first lines of the file
        /// </summary>
        /// <param name="counter">the n first lines to be returned</param>
        /// <param name="path">the path of the file to read</param>
        /// <returns></returns>
        public string ReadLinesFromFile(int counter, string path)
        {
            string result = "";
            string ln = null;
            StreamReader file = new StreamReader(path);

            //todo: research do while loop

            try
            {
                ln = file.ReadLine();
                while (ln != null && counter-- > 0)
                {
                    ln = file.ReadLine();
                }
            }
            catch (Exception e)
            {
                Console.Write("Could not read from file with path {0}\n{1}", path, e.Source);
            }

            return ln;
        }
    }
}