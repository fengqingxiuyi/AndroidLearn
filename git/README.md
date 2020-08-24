# git

git操作说明

## 两个git仓库合并

案例：将repo1合并到repo2，并保留repo1的所有提交记录

1. git clone repo2的仓库地址
2. cd repo2
3. git remote add other repo1的仓库地址
4. git fetch other
5. git checkout -b repo1 other/master
6. git checkout master
7. git merge repo1
8. 解决冲突
9. 执行 commit & push 命令

