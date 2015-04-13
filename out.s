	# Authors: Tyler Harley, Harry Bartlett, Terrence Tan
	# Date: Sun Apr 12 21:08:44 EDT 2015
	.data
	.globl	gc_flag
gc_flag:
	.word	0
	.text
	.globl	main
main:
	li $v0 33
	li $a3 75
	li $a1 120
	li $a2 0
	# Notes Start
	# Song starts here
	# Load tempo/instrument/octave/volume for phrase
	add $sp $sp -4
	li $t0 500
	sw $t0 4($sp)
	add $sp $sp -4
	li $t0 0
	sw $t0 4($sp)
	add $sp $sp -4
	li $t0 3
	sw $t0 4($sp)
	add $sp $sp -4
	li $t0 75
	sw $t0 4($sp)
	# PhraseCall
	jal BbScale
	add $sp $sp 16
	# Phrase Dispatch End
	li $v0 10
	syscall
BbScale:
	li $a1 4000
	li $t0 3
	mul $t0 $t0 12
	add $t0 $t0 2
	add $a0 $t0 9
	add $a0 $a0 -1
	# Playing B
	syscall
	li $a1 2000
	li $t0 3
	mul $t0 $t0 12
	add $t0 $t0 3
	add $a0 $t0 9
	# Playing C
	syscall
	li $a1 2000
	li $t0 3
	mul $t0 $t0 12
	add $t0 $t0 5
	add $a0 $t0 9
	# Playing D
	syscall
	li $a1 1000
	li $t0 3
	mul $t0 $t0 12
	add $t0 $t0 7
	add $a0 $t0 9
	add $a0 $a0 -1
	# Playing E
	syscall
	li $a1 1000
	li $t0 3
	mul $t0 $t0 12
	add $t0 $t0 8
	add $a0 $t0 9
	# Playing F
	syscall
	li $a1 1000
	li $t0 3
	mul $t0 $t0 12
	add $t0 $t0 10
	add $a0 $t0 9
	# Playing G
	syscall
	li $a1 1000
	li $t0 4
	mul $t0 $t0 12
	add $t0 $t0 0
	add $a0 $t0 9
	# Playing A
	syscall
	li $a1 4000
	li $t0 4
	mul $t0 $t0 12
	add $t0 $t0 2
	add $a0 $t0 9
	add $a0 $a0 -1
	# Playing B
	syscall
	jr $ra
