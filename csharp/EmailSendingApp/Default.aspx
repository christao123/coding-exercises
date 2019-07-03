<%@ Page Title="Home Page" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="Default.aspx.cs" Inherits="WebApplication_._Default" %>

<asp:Content ID="BodyContent" ContentPlaceHolderID="MainContent" runat="server">

    <div class="jumbotron">
        <h1>My App</h1>
        <p class="lead">goml.</p>
        <p class="lead">
            <asp:TextBox ID="TextBox1" runat="server"></asp:TextBox>
        </p>
    </div>

</asp:Content>
