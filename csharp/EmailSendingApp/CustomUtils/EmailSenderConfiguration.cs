using System;
using System.Xml;

namespace WebApplication_.CustomUtils
{
    /// <summary>
    /// This class contains all the details needed to send an email
    /// </summary>
    public class EmailSenderConfiguration
    {
        public string FromAddress { get; set; }
        public string SmtpServer { get; set; }
        public int Port { get; set; }
        public bool EnableSsl { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }
        //todo: this password system is not secure!
        //todo: add attachment functionality

        /// <summary>
        /// This constructor will need all the content of a configuration file
        /// </summary>
        /// <param name="fromAddress">The email address of the sender</param>
        /// <param name="smtpServer">the smtp server address</param>
        /// <param name="port">the server port to connect to</param>
        /// <param name="enableSsl">true if ssl has to be enabled</param>
        /// <param name="username">The username of the sender's account</param>
        /// <param name="password">The password of the sender's account</param>
        public EmailSenderConfiguration(
            string fromAddress, string smtpServer,
            int port, bool enableSsl,
            string username, string password)
        {
            FromAddress = fromAddress;
            SmtpServer = smtpServer;
            Port = port;
            EnableSsl = enableSsl;
            Username = username;
            Password = password;
        }

        /// <summary>
        /// Constructor that uses an xml file of the format:
        ///<?xml version = "1.0" ?>
        /// < EmailConfiguration >
        ///     < FromAddress > bestlookingproject@gmail.com </ FromAddress >
        ///     <SMTPServer>
        ///         <Address>smtp.google.com</Address>
        ///         <Port>587</Port>
        ///         <Credentials>
        ///             <Username>bestlookingproject@gmail.com</Username>
        ///             <Password>password</Password>
        ///             <EnableSsl>True</EnableSsl>
        ///         </Credentials>
        ///     </SMTPServer>
        /// </EmailConfiguration>
        /// </summary>
        /// <param name="xmlConfigFilePath"></param>
        public EmailSenderConfiguration(string xmlConfigFilePath)
        {
            XmlDocument xmlDoc = new XmlDocument();
            xmlDoc.Load(xmlConfigFilePath);
            try
            {
                FromAddress = xmlDoc.GetElementsByTagName("FromAddress")[0].InnerText;
                SmtpServer = xmlDoc.GetElementsByTagName("SMTPServerAddress")[0].InnerText;
                Port = Int32.Parse(xmlDoc.GetElementsByTagName("Port")[0].InnerText);
                EnableSsl = Boolean.Parse(xmlDoc.GetElementsByTagName("EnableSsl")[0].InnerText);
                Username = xmlDoc.GetElementsByTagName("Username")[0].InnerText;
                Password = xmlDoc.GetElementsByTagName("Password")[0].InnerText;
            }
            catch(Exception e)
            {
                Console.WriteLine("Failed loading email sender configuration xml file. \n{0}", e.Message);
            }
        }


    }
}