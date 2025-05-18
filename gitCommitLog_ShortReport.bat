(
    	echo "===== Commit Summary ====="
	git shortlog --summary --numbered --all --no-merges
    	echo -e "===== Detailed Commit Log ====="
    	git log --pretty=format:'%h was %an (%ae), %at, %ar, message: %s'
) > git_commit_report.txt