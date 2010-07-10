#!/bin/sh

rsync -avz --delete --exclude-from=rsyncignore `pwd`/ casey@casey-jones.org:/var/www/jblux_django/

