from django import forms

class LoginForm(forms.Form):
    username = forms.CharField(max_length=50)
    password = forms.CharField(max_length=50, widget=forms.PasswordInput(render_value=False))

class RegisterForm(forms.Form):
    username = forms.CharField(max_length=50)
    email = forms.CharField(max_length=75)
    password = forms.CharField(max_length=50, widget=forms.PasswordInput(render_value=False))
    password2 = forms.CharField(max_length=50, widget=forms.PasswordInput(render_value=False))

