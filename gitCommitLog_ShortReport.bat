(
    	echo "===== Commit Summary (no merges) ====="
	git shortlog --summary --numbered --all --no-merges
) > git_commit_report.txt