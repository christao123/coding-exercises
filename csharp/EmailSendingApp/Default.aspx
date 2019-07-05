<%@ Page Title="Home Page" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="Default.aspx.cs" Inherits="WebApplication_._Default" %>

<asp:Content ID="BodyContent" ContentPlaceHolderID="MainContent" runat="server">
    <asp:Image class="center-block" src="Images/logo-circle.png" ID="Banner" runat="server" />
    <div align="center">
        <!-- Get started Button-->
        <asp:Button ID="GetStarted" OnCommand="GetStarted_click" Style="margin-left: auto; margin-right: auto;" class="button" runat="server" BorderStyle="Solid" EnableViewState="False" Text="GET STARTED " ToolTip="on you go, chop chop" />
        <!-- Send Email Form-->
        <!--TODO: MAKE EMAILS CONFIGURABLE and remove placeholders-->
        <div class="row">
            <div class="center-block form-group" style="margin-left: auto; margin-right: auto;" id="EmailForm" runat="server">
                <label>Recipient address</label>

                <asp:DropDownList Style="width: 100%;" class="form-control" ID="FromAddressDropDownList" runat="server">
                    <asp:ListItem Value="Choose the recipient" Selected="True"></asp:ListItem>
                    <asp:ListItem Value="delbussodanilo98@gmail.com"></asp:ListItem>
                    <asp:ListItem Value="Robbie.Lambert@BMWGroup.com"></asp:ListItem>
                    <asp:ListItem Value="Andreas.M.Geier@bmwgroup.com"></asp:ListItem>
                    <asp:ListItem Value="Adrian.Radulescu@BMWGroup.com"></asp:ListItem>
                </asp:DropDownList>
                <br />
                <br />
                <label>Subject</label>

                <asp:TextBox Style="width: 100%" class="form-control" ID="SubjectTextBox" placeholder="File Created in C#" runat="server"></asp:TextBox>
                <br />
                <label>Message</label>

                <asp:TextBox Style="width: 100%" class="form-control" ID="MessageTextBox" placeholder="Hello (Name), here is the text file. Regards (Name)" runat="server" Rows="1" TextMode="MultiLine" Height="150px"></asp:TextBox>
                <br />
                <label>Select Attachments</label>

                <asp:FileUpload Width="1000px" ID="FileUpload" runat="server" AllowMultiple="True" />
                <br />
                <asp:Button ID="SendEmail" OnCommand="SendEmail_click" Style="margin-left: auto; margin-right: auto;" class="button" runat="server" BorderStyle="Solid" EnableViewState="False" Text="Send" ToolTip="on you go, chop chop" />
            </div>
    </div>
    </div>

</asp:Content>
