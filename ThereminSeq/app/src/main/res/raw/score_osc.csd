<CsoundSynthesizer>
<CsOptions>
-o dac -+rtmidi=null -d -+msg_color=0 -M0 -m0
</CsOptions>
<CsInstruments>
nchnls=2
0dbfs=1
ksmps=32
sr = 48000

instr 1

;oscillator
a1 vco2 .5, p4
;reverb
aL, aR reverbsc a1, a1, .72, 5000
;output
out aL, aR

endin

</CsInstruments>
<CsScore>
f1 0 16384 10 1

i1 0 360000

</CsScore>
</CsoundSynthesizer>
