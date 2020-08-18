#!/bin/bash
export APP=eMPWR-VA
export HOME_REPORTS=$PWD/reports
echo $HOME_REPORTS

ls $HOME_REPORTS/$APP | grep 'EMPWR' > $HOME_REPORTS/$APP/reports.txt
    
while read -r rpt; 
do
    echo $rpt
    case "$rpt" in
        *"aggregate_rpt"*)
          continue
        ;;
    esac
    export ISSUE=$(echo $rpt | cut -c 1-11 | sed s/_//)
    export AGGR_RPT_PATH=$(echo $HOME_REPORTS/$APP/)
    export AGGR_RPT_SUFFIX=_aggregate_rpt.csv
    export AGGR_RPT=${AGGR_RPT_PATH}${ISSUE}${AGGR_RPT_SUFFIX}
    echo   $AGGR_RPT

    if [ -f $AGGR_RPT ] ; then
        echo "appending metrics to report "$AGGR_RPT
    else
        echo "creating new aggregate report "$AGGR_RPT
    fi

    case "$ISSUE" in
        *"1022"*)
            echo ",LOGIN,LOAD COMPONENT,LOAD PARTICIPANT,LOAD DETAILS, LOAD NOTES, TOTAL" > $AGGR_RPT
            ;;
        *)
            echo ",LOGIN,LOAD COMPONENT,LOAD ALL,LOAD ONE,LOAD DETAILS,TOTAL" > $AGGR_RPT
    esac

    cat $HOME_REPORTS/$APP/$rpt >> $AGGR_RPT
    rm -f $HOME_REPORTS/$APP/$rpt
done < $HOME_REPORTS/$APP/reports.txt
rm -f $HOME_REPORTS/$APP/reports.txt
