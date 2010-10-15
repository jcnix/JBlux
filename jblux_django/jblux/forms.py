from django import forms
from django.core.validators import MinLengthValidator, validate_email
from django.core.exceptions import ValidationError
from jblux_django.jblux.models import User, Race, Class, Character

class NewUserNameField(forms.CharField):
    def validate(self, value):
        super(NewUserNameField, self).validate(value)
        validate_name(value)

class NewEmailField(forms.EmailField):
    def validate(self, value):
        super(NewEmailField, self).validate(value)
        validate_new_email(value)

class NewCharacterField(forms.CharField):
    def validate(self, value):
        super(NewCharacterField, self).validate(value)
        validate_character(value)

class LoginForm(forms.Form):
    username = forms.CharField(max_length=50)
    password = forms.CharField(max_length=50, widget=forms.PasswordInput(render_value=False))

class RegisterForm(forms.Form):
    min_name = MinLengthValidator(3)
    min_pass = MinLengthValidator(6)

    username = NewUserNameField(max_length=50, validators=[min_name])
    email = NewEmailField()
    password = forms.CharField(max_length=50, validators=[min_pass], widget=forms.PasswordInput(render_value=False))
    password2 = forms.CharField(max_length=50, validators=[min_pass], widget=forms.PasswordInput(render_value=False))

    def clean(self):
        super(forms.Form,self).clean()
        if 'password' in self.cleaned_data and 'password2' in self.cleaned_data:
            if self.cleaned_data['password'] != self.cleaned_data['password2']:
                self._errors['password'] = [u'Passwords must match']
                self._errors['password2'] = [u'Passwords must match']

        return self.cleaned_data

class CharacterForm(forms.Form):
    min_name = MinLengthValidator(3)

    name = NewCharacterField(max_length=50, validators=[min_name])
    race = forms.ModelChoiceField(Race.objects.all(), empty_label=None)
    class_t = forms.ModelChoiceField(Class.objects.all(), empty_label=None)

class SelectCharacterForm(forms.Form):
    character = forms.ModelChoiceField(queryset=Character.objects.none(), empty_label=None)

    def __init__(self, *args, **kwargs):
        user = kwargs.pop('user')
        super(SelectCharacterForm, self).__init__(*args, **kwargs)
        self.fields["character"].queryset = Character.objects.filter(user=user)

def validate_name(value):
    try:
        user = User.objects.get(username=value)
    except User.DoesNotExist:
        return

    raise ValidationError('Username is in use')

def validate_new_email(value):
    try:
        user = User.objects.get(email=value)
    except User.DoesNotExist:
        return

    raise ValidationError('Email is in use')

def validate_character(value):
    try:
        char = Character.objects.get(name=value)
    except Character.DoesNotExist:
        return

    raise ValidationError('Character name is in use')

