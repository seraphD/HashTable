# Hash Table

Memory management practicing project.

## Overview

The purpose of this project is to use an efficient way to store and remove information in memory pool. Each record will be stored in a memory pool

## command

add {name}

Add a key into memory pool, and also insert a record in hash table. When adding new names, the sysytem will first check duplicates in hashtable.

delete {name}

Delete a record from pool. Note that this command will delete the record in hash table and memory pool.

update add {name}\<SEP>{field_name}\<SEP>{field_value}
  
Each record can have multiple pairs of field name and field value. If there is a record key, it can be updated to key\<SEP>f_key\<SEP>f_value.
  
update delete {name}\<SEP>{field_name}

This command is similar with the previous one.

print hashtable

print key stored in hash table

print blocks

print free blocks in memory pool.

## Algorithms used

Hash function: sfold.

Confict solving function: quadratic.

Memory management: buddy method.

## Class

### CommandParser

Used to parse command lines from files. Note that this class will transform command lines to regular form, including removing extra blanks and making words into Canonical form.

### MemPool

Maintaining a continues memory block. The size will be a power of 2. Using a chain list to store free blocks in pool

### Handle

A ref of a record in the memory pool

### Hash

Hash table. Whenever add or delete or update a record, the system will first check is there is a duplicate in hash table.
