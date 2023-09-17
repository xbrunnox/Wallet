#!/bin/bash
ssh-copy-id root@admin.versatil-ia.com.br
# mvn clean
# mvn package
# rsync -avzhe ssh --progress target/wallet.war root@admin.versatil-ia.com.br:/storage/metatrader
scp target/wallet.war root@admin.versatil-ia.com.br:/storage/metatrader
scp wallet.war.dodeploy root@admin.versatil-ia.com.br:/storage/metatrader
