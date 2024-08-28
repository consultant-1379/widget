#!/bin/bash

if [ "$2" == "" ]; then
        echo usage: $0 \<Module\> \<Branch\> \<Workspace\>
        exit -1
else
        versionProperties=install/version.properties
        theDate=\#$(date +"%c")
        module=$1
        branch=$2
        workspace=$3
fi

function getProductNumber {
        product=`cat $PWD/build.cfg | grep $module | grep $branch | awk -F " " '{print $3}'`
}


function setRstate {

        revision=`cat $PWD/build.cfg | grep $module | grep $branch | awk -F " " '{print $4}'`

        if git tag | grep $product-$revision; then
                rstate=`git tag | grep $revision | tail -1 | sed s/.*-// | perl -nle 'sub nxt{$_=shift;$l=length$_;sprintf"%0${l}d",++$_}print $1.nxt($2) if/^(.*?)(\d+$)/';`
        else
                ammendment_level=01
                rstate=$revision$ammendment_level
       fi
       echo "Building R-State:$rstate"

}

function nexusDeploy {
	#RepoURL=http://eselivm2v214l.lmera.ericsson.se:8081/nexus/content/repositories/releases
	RepoURL=https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/repositories/assure-releases

	GroupId=com.ericsson.gwt
	ArtifactId=widget
	
	echo "****"	
	echo "Deploying the jar /widget-1.0.jar as ${ArtifactId}${rstate}.jar to Nexus...."
	echo "****"	

    mvn deploy:deploy-file \
        -Durl=${RepoURL} \
        -DrepositoryId=assure-releases \
        -Dpackaging=jar \
        -DgroupId=${GroupId} \
        -Dversion=${rstate} \
        -DartifactId=${ArtifactId} \
        -Dfile=target/widget-1.0.jar

}

getProductNumber
setRstate
git checkout $branch
git pull

mvn clean install
rsp=$?


if [ $rsp == 0 ]; then

git tag $product-$rstate
git pull
git push --tag origin $branch
  mkdir install
  touch $versionProperties
  echo $theDate >> $versionProperties
  echo module.name=$module >> $versionProperties
  echo module.version=$rstate >> $versionProperties
  echo build.tag=b999 >> $versionProperties
  echo author=vobadm8 >> $versionProperties
  echo module.build=999 >> $versionProperties
  echo product.number=$product >> $versionProperties
  echo product.label=$product-$rstate >> $versionProperties

  zip widget_$rstate.zip install/* target/*.jar
  cp widget_$rstate.zip /home/$USER/eniq_events_releases

  nexusDeploy

  \rm -rf widget_$rstate.zip
  \rm -rf install
fi

exit $rsp

