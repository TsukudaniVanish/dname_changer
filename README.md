# Dname-changer 

### Requirements 


- openjdk 17 or upper
- google drive api auth
    -- ref: https://developers.google.com/drive/api/quickstart/java

### Usage

- `sbt run` and `help` will show useage text

```
dname
Usage: dname [ls|rename|cut-copy-of|help|quit]

Command: ls [options]
list up contents in your drive
  -d, --folder-only        show folders
  -s, --page-size <value>  page size
  -n, --page-num <value>   max page number to read
  -w, --name <value>       keywords contained in name
  -p, --parent-id <value>  set parent id and constrain parent of segments
Command: rename [options]
update filename of given id
  -n, --new-name <value>   new file name
  -f-id, --file-id <value>
                           file id of target file
Command: cut-copy-of [options]
cut 'のコピー' from contents of specified folder by id
  -p-id, --parent-id <value>
                           parent id taht specifies folder
Command: help
show useage text
Command: quit
quit this app
```

### Note 
    

- In Useage text, allowed commands are like `dname ls` but you don't need `dname` prefix if you run app.

