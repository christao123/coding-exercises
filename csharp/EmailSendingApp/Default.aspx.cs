using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using static WebApplication_.FileManager;

namespace WebApplication_
{
    public partial class _Default : Page
    {
        private string rootPath = HttpContext.Current.Server.MapPath("~");
        protected void Page_Load(object sender, EventArgs e)
        {
           string t = new FileManager().ReadFromFile(rootPath + "/App_Data/file.txt");
           TextBox1.Text = t;
           new FileManager().WriteToFile(rootPath + "/App_Data/new_file.txt", "oioioi");
        }
    }
}