using System;
using System.Collections;
using System.Net.Mail;
using System.Xml;
using MailMessage = System.Net.Mail.MailMessage;

namespace WebApplication_.CustomUtils
{
    /// <summary>
    /// The Email sender contains all the info needed to send emails from a client
    /// It can send individual emails with attachments
    /// </summary>
    public class EmailSender
    {
        public string FromAddress { get; set; }
        public string SmtpServer { get; set; }
        public int Port { get; set; }
        public bool EnableSsl { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }

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
        /// <param name="xmlConfigFilePath">the path of the sender configuration file</param>
        public EmailSender(string xmlConfigFilePath)
        {
            XmlDocument xmlDoc = new XmlDocument();
            xmlDoc.Load(xmlConfigFilePath);
            try
            {
                FromAddress = xmlDoc.GetElementsByTagName("FromAddress")[0].InnerText;
                SmtpServer = xmlDoc.GetElementsByTagName("SMTPServerAddress")[0].InnerText;
                Port = int.Parse(xmlDoc.GetElementsByTagName("Port")[0].InnerText);
                EnableSsl = bool.Parse(xmlDoc.GetElementsByTagName("EnableSsl")[0].InnerText);
                Username = xmlDoc.GetElementsByTagName("Username")[0].InnerText;
                Password = xmlDoc.GetElementsByTagName("Password")[0].InnerText;
            }
            catch (Exception e)
            {
                Console.WriteLine("Failed loading email sender configuration xml file. \n{0}", e.Message);
            }
        }

        /// <summary>
        /// This constructor will need all the content of a sender configuration
        /// </summary>
        /// <param name="fromAddress">The email address of the sender</param>
        /// <param name="smtpServer">the smtp server address</param>
        /// <param name="port">the server port to connect to</param>
        /// <param name="enableSsl">true if ssl has to be enabled</param>
        /// <param name="username">The username of the sender's account</param>
        /// <param name="password">The password of the sender's account</param>
        public EmailSender(
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
        ///  Send an email according to the content of the configuration file.
        /// Returns true if the email sending was successful
        /// </summary>
        /// <param name="toAddress">the receiver's email address</param>
        /// <param name="subject">the subject of the email</param>
        /// <param name="body">the main content of the email</param>
        /// <param name="attachmentPaths">an array containing the attachments' paths</param>
        /// <param name="attachments">all the attachments of the email</param>
        public bool SendEmail(string toAddress, string subject, string body, ArrayList attachments)
        {
            try
            {
                MailMessage mail = new MailMessage();
                SmtpClient smtpServerClient = new SmtpClient(SmtpServer);
                mail.From = new MailAddress(FromAddress);
                mail.To.Add(toAddress);
                mail.Subject = subject;
                mail.Body = body;
                
                System.Diagnostics.Debug.WriteLine(EnableSsl);
                smtpServerClient.Port = Port;
                smtpServerClient.Credentials = new System.Net.NetworkCredential(Username, Password);
                smtpServerClient.EnableSsl = EnableSsl;

                foreach (Attachment attachment in attachments)
                {
                    mail.Attachments.Add(attachment);
                }

                smtpServerClient.Send(mail);
            }
            catch (Exception e)
            {
                Console.WriteLine("Failed sending email. \n{0}", e.Message);
                return false;
            }

            return true;
        }
    }
}
