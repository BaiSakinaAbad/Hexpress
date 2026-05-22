import React, { useState, useMemo, useEffect } from 'react';
import { Screen, Product, Customer } from './types';
import { motion, AnimatePresence } from 'framer-motion';
import { ALL_PRODUCTS, INITIAL_CUSTOMERS, LOCATIONS } from './data';
import { solveKnapsack, naiveStringMatch, mergeSortCustomers, solveTSP } from './utils';
import { CheckCircle2, PackageX, ArrowUpDown, Play, Pause } from 'lucide-react';
import confetti from 'canvas-confetti';

const Typewriter = ({ text, delay = 0, speed = 40, skip = false }: { text: string, delay?: number, speed?: number, skip?: boolean }) => {
  const [displayedText, setDisplayedText] = useState('');

  useEffect(() => {
    if (skip) {
      setDisplayedText(text);
      return;
    }

    let i = 0;
    setDisplayedText('');
    let interval: ReturnType<typeof setInterval>;
    
    const startTimeout = setTimeout(() => {
      interval = setInterval(() => {
        setDisplayedText(text.substring(0, i + 1));
        i++;
        if (i >= text.length) {
          clearInterval(interval);
        }
      }, speed);
    }, delay);
    
    return () => {
      clearTimeout(startTimeout);
      if (interval) clearInterval(interval);
    };
  }, [text, delay, speed, skip]);

  return <>{displayedText}</>;
};

export default function App() {
  const [currentScreen, setCurrentScreen] = useState<Screen>('intro1');
  const [skipIntro, setSkipIntro] = useState(false);
  const [maxWeight, setMaxWeight] = useState<number>(15);
  const [knapsackResult, setKnapsackResult] = useState<{ selected: Product[], solved: boolean }>({ selected: [], solved: false });
  const [searchQuery, setSearchQuery] = useState('');
  const [sortBy, setSortBy] = useState<'name' | 'value' | 'weight'>('name');
  
  // TSP State
  const [tspRoute, setTspRoute] = useState<string[]>([]);
  const [simulationStep, setSimulationStep] = useState(0);
  const [showCredits, setShowCredits] = useState(false);
  const [isPlaying, setIsPlaying] = useState(false);
  const audioRef = React.useRef<HTMLAudioElement>(null);

  useEffect(() => {
    if (currentScreen === 'exit' && !showCredits) {
      const duration = 500;
      const end = Date.now() + duration;

      const frame = () => {
        confetti({
          particleCount: 5,
          angle: 60,
          spread: 55,
          origin: { x: 0, y: 1 },
          zIndex: 100,
        });
        confetti({
          particleCount: 5,
          angle: 120,
          spread: 55,
          origin: { x: 1, y: 1 },
          zIndex: 100,
        });

        if (Date.now() < end) {
          requestAnimationFrame(frame);
        }
      };
      
      frame();
    }
  }, [currentScreen, showCredits]);

  const toggleAudio = () => {
    if (audioRef.current) {
      if (isPlaying) {
        audioRef.current.pause();
      } else {
        audioRef.current.play();
      }
      setIsPlaying(!isPlaying);
    }
  };

  const handleKnapsackSolve = () => {
    let finalWeight = maxWeight;
    if (finalWeight < 9) finalWeight = 9;
    if (finalWeight > 10) finalWeight = 10;
    setMaxWeight(finalWeight);
    const selected = solveKnapsack(ALL_PRODUCTS, finalWeight);
    setKnapsackResult({ selected, solved: true });
  };

  const customersInKnapsack = useMemo(() => {
    return INITIAL_CUSTOMERS.filter(c => knapsackResult.selected.some(p => p.name === c.productName));
  }, [knapsackResult.selected]);

  const sortedCustomers = useMemo(() => {
    return mergeSortCustomers([...customersInKnapsack], sortBy);
  }, [customersInKnapsack, sortBy]);

  const displayedCustomers = useMemo(() => {
    if (!searchQuery) return sortedCustomers;
    return sortedCustomers.filter(c => 
      naiveStringMatch(c.name, searchQuery) || 
      naiveStringMatch(c.location, searchQuery) ||
      naiveStringMatch(c.productName, searchQuery)
    );
  }, [sortedCustomers, searchQuery]);

  const handleStartTSP = () => {
    const locationsToVisit = Array.from(new Set(customersInKnapsack.map(c => c.location)));
    const bestRoute = solveTSP(locationsToVisit, LOCATIONS);
    setTspRoute(bestRoute);
    setSimulationStep(0);
    setCurrentScreen('tsp');
  };

  return (
    <div 
      className="min-h-screen w-full relative flex items-center justify-center p-4 font-sans font-bold text-[#802a2a]"
      onClick={() => (currentScreen === 'intro1' || currentScreen === 'intro2') && setSkipIntro(true)}  
    >
      {/* Audio Setup */}
      <audio ref={audioRef} src="/bgmusic.mp3" loop />
      <button 
        onClick={(e) => {
          e.stopPropagation();
          toggleAudio();
        }}
        className="fixed top-4 left-4 z-50 bg-[#faeec8] p-3 rounded-full shadow-md border-2 border-black hover:bg-white transition-colors flex items-center justify-center cursor-pointer"
        aria-label={isPlaying ? "Pause music" : "Play music"}
      >
        {isPlaying ? <Pause className="w-6 h-6 text-[#8b1a1a]" /> : <Play className="w-6 h-6 text-[#8b1a1a]" />}
      </button>

      {/* Background */}
      {currentScreen === 'intro1' || currentScreen === 'intro2' ? (
        <div 
          className="absolute inset-0 z-0 bg-cover bg-center pointer-events-none" 
          style={{ backgroundImage: "url('/kikibg.gif')" }} 
        />
      ) : currentScreen === 'exit' ? (
        <div 
          className="absolute inset-0 z-0 bg-cover bg-center pointer-events-none opacity-80" 
          style={{ backgroundImage: "url('/exitbackground.gif')" }} 
        />
      ) : (
        <div 
          className="absolute inset-0 z-0 bg-cover bg-center pointer-events-none opacity-80" 
          style={{ backgroundImage: "url('/clouds.gif')" }} 
        />
      )}

      <AnimatePresence mode="wait">
        {currentScreen === 'intro1' && (
          <motion.div
            key="intro1"
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            exit={{ opacity: 0, scale: 1.05 }}
            className="z-10 p-8 w-[600px] max-w-[95vw] h-[350px] text-center flex flex-col items-center justify-between"
          >
            <h1 className="text-5xl md:text-6xl font-caveat font-bold drop-shadow-md text-black tracking-wide leading-tight mt-8" style={{ textShadow: "2px 2px 0px white, -1px -1px 0px white, 1px -1px 0px white, -1px 1px 0px white, 1px 1px 0px white", minHeight: '2.5em' }}>
              <Typewriter text="Would you like to join Kiki on her adventure?" speed={45} skip={skipIntro} />
            </h1>
            <motion.button 
              key={skipIntro ? "skip1" : "wait1"}
              initial={{ opacity: skipIntro ? 1 : 0, y: skipIntro ? 0 : 10 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: skipIntro ? 0 : 2.2, duration: 0.3 }}
              onClick={(e) => {
                e.stopPropagation();
                setSkipIntro(false);
                setCurrentScreen('intro2');
              }}
              className="mb-8 px-8 py-3 bg-[#cdebf9] border-2 border-[#1c305c] rounded-full text-2xl shadow-[4px_4px_0_0_#1c305c] hover:translate-x-1 hover:translate-y-1 hover:shadow-none transition-all"
            >
              <span className="text-[#9e1c1c]">Yes, Let's go!</span>
            </motion.button>
          </motion.div>
        )}

        {currentScreen === 'intro2' && (
          <motion.div
            key="intro2"
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            exit={{ opacity: 0, scale: 1.05 }}
            className="z-10 p-8 w-[800px] max-w-[95vw] h-auto min-h-[450px] bg-white/80 rounded-2xl shadow-2xl border-4 border-black text-center flex flex-col items-center justify-between relative overflow-hidden"
          >
            <div className="flex w-full items-start gap-4 flex-1 mt-4 z-10 relative">
               <motion.img 
                 src="/kikinobg.gif" 
                 alt="Kiki Flying" 
                 className="w-96 h-96 object-contain drop-shadow-md shrink-0 mt-4" 
                 animate={{ y: [-5, 5, -5] }}
                 transition={{ duration: 4, repeat: Infinity, ease: "easeInOut" }}
               />
               <div 
                 className="flex-1 text-2xl text-left leading-relaxed text-[#2c1818] font-bold"
                 style={{ textShadow: "2px 2px 0px white, -1px -1px 0px white, 1px -1px 0px white, -1px 1px 0px white, 1px 1px 0px white" }}
               >
                 <p className="mb-4 text-[#8b1a1a] min-h-[70px]">
                    <Typewriter text="Hi! I'm Kiki, a witch-in-training, and I run a little delivery service in this town." speed={35} skip={skipIntro} />
                 </p>
                 <p className="mb-4 min-h-[70px]">
                    <Typewriter text="But today, I have so many packages to deliver, and I could really use some help!" delay={3200} speed={35} skip={skipIntro} />
                 </p>
                 <p className="text-xl opacity-90 italic text-black min-h-[70px]">
                    <Typewriter text="Your task is to help me pick the most valuable items my basket can carry, find the customers, and plan the shortest delivery route!" delay={6500} speed={35} skip={skipIntro} />
                 </p>
               </div>
            </div>
            <motion.button 
              key={skipIntro ? "skip2" : "wait2"}
              initial={{ opacity: skipIntro ? 1 : 0, y: skipIntro ? 0 : 10 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: skipIntro ? 0 : 11.5, duration: 0.3 }}
              onClick={(e) => {
                 e.stopPropagation();
                 setSkipIntro(false);
                 setCurrentScreen('knapsack');
              }}
              className="px-8 py-3 bg-[#cdebf9] border-2 border-[#1c305c] rounded-full text-2xl shadow-[4px_4px_0_0_#1c305c] hover:translate-x-1 hover:translate-y-1 hover:shadow-none transition-all z-10 relative mb-4"
            >
              <span className="text-[#9e1c1c]">I'm ready to help!</span>
            </motion.button>
          </motion.div>
        )}

        {currentScreen === 'knapsack' && (
          <motion.div
            key="knapsack"
            initial={{ opacity: 0, x: 50 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -50 }}
            className="z-10 bg-[#faeec8] border-4 border-black rounded-xl p-8 w-full max-w-5xl shadow-xl flex flex-col items-center gap-6 overflow-auto max-h-[90vh]"
          >
            <h2 className="text-3xl text-[#8b1a1a]">Kiki's Basket Can Only Hold So Much!</h2>
            <p className="text-xl">Please enter the maximum weight Kiki can carry (9-10kg):</p>
            
            <div className="flex gap-4 items-center">
              <input 
                type="text" 
                inputMode="numeric"
                pattern="[0-9]*"
                className="border-2 border-black p-2 text-xl w-32 bg-white text-center"
                value={maxWeight || ''}
                onKeyDown={(e) => {
                  if (!/[0-9]/.test(e.key) && !['Backspace', 'ArrowLeft', 'ArrowRight', 'Tab', 'Delete'].includes(e.key)) {
                    e.preventDefault();
                  }
                }}
                onChange={(e) => {
                  const val = parseInt(e.target.value.replace(/[^0-9]/g, ''), 10);
                  setMaxWeight(isNaN(val) ? 0 : val);
                  if (knapsackResult.solved) setKnapsackResult({ selected: [], solved: false }); // Reset on change
                }}
              />
              <button 
                onClick={handleKnapsackSolve}
                className="px-6 py-2 bg-[#dcb18c] border-2 border-black rounded-lg text-xl hover:bg-opacity-80 transition-all text-[#682414]"
              >
                Calculate (Solve Knapsack)
              </button>
            </div>

            <div className="w-full bg-white/50 border-2 border-black p-4 mt-2 overflow-x-auto">
              <div className="grid grid-cols-4 min-w-[600px] gap-2 border-b-2 border-black pb-2 text-lg font-black text-black">
                <div>Product Name</div>
                <div className="text-center">Weight (kg)</div>
                <div className="text-center">Value (£)</div>
                <div className="text-center">Status</div>
              </div>
              <div className="max-h-[30vh] overflow-y-auto mt-2 flex flex-col gap-2">
                {ALL_PRODUCTS.map((prod) => {
                  const isSelected = knapsackResult.selected.some(p => p.id === prod.id);
                  const statusBg = knapsackResult.solved ? (isSelected ? 'bg-green-100 border-green-500' : 'bg-red-50 border-red-300 opacity-60') : 'bg-white border-black/20';
                  
                  return (
                    <motion.div 
                      key={prod.id}
                      animate={knapsackResult.solved ? { scale: isSelected ? 1.02 : 0.98 } : {}}
                      className={`grid grid-cols-4 min-w-[600px] items-center p-2 rounded border-2 transition-all duration-300 ${statusBg}`}
                    >
                      <div className={`font-normal ${isSelected ? 'font-black text-green-800' : ''}`}>{prod.name}</div>
                      <div className="text-center font-normal">{prod.weight}</div>
                      <div className="text-center font-normal">{prod.value}</div>
                      <div className="flex justify-center items-center font-normal">
                        {!knapsackResult.solved && <span className="text-gray-400">-</span>}
                        {knapsackResult.solved && isSelected && (
                          <div className="flex items-center gap-1 text-green-600 font-bold bg-green-200 px-2 py-1 rounded">
                            <CheckCircle2 size={18} /> First Priority
                          </div>
                        )}
                        {knapsackResult.solved && !isSelected && (
                          <div className="flex items-center gap-1 text-red-500 bg-red-100 px-2 py-1 rounded">
                            <PackageX size={18} /> Too Heavy / Low Value
                          </div>
                        )}
                      </div>
                    </motion.div>
                  )
                })}
              </div>
            </div>

            <AnimatePresence>
              {knapsackResult.solved && (
                <motion.div 
                  initial={{ opacity: 0, y: 10 }}
                  animate={{ opacity: 1, y: 0 }}
                  className="w-full flex justify-between items-center bg-[#cdebf9] border-2 border-black p-4 mt-2"
                >
                  <div className="text-xl">
                    Total Value Selected: <span className="text-2xl font-black text-green-700">£{knapsackResult.selected.reduce((acc, p) => acc + p.value, 0)}</span> | 
                    Total Weight: <span className="text-2xl font-black">{knapsackResult.selected.reduce((acc, p) => acc + p.weight, 0)}</span> / {maxWeight}kg
                  </div>
                  <button 
                    onClick={() => setCurrentScreen('search')}
                    className="px-6 py-2 bg-[#faeec8] border-2 border-black rounded-lg text-xl hover:bg-white transition-all text-[#8b1a1a]"
                  >
                    Proceed to Delivery List
                  </button>
                </motion.div>
              )}
            </AnimatePresence>
          </motion.div>
        )}

        {currentScreen === 'search' && (
          <motion.div
            key="search"
            initial={{ opacity: 0, x: 50 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -50 }}
            className="z-10 w-full h-full max-h-[90vh] max-w-6xl flex flex-col gap-4 mt-2"
          >
            <div className="flex justify-between items-center bg-[#faeec8] border-2 border-black p-4 shadow-md shrink-0">
              <div className="text-2xl text-[#8b1a1a]">Customer List & Sorting</div>
              <div className="flex items-center gap-4">
                <span className="text-xl">Sort By:</span>
                <select 
                  className="border-2 border-black p-2 bg-white text-lg cursor-pointer font-sans"
                  value={sortBy}
                  onChange={(e) => setSortBy(e.target.value as any)}
                >
                  <option value="name">Name (Alphabetical)</option>
                  <option value="value">Value (Highest to Lowest)</option>
                  <option value="weight">Weight (Lightest to Heaviest)</option>
                </select>
              </div>
            </div>

            <div className="flex justify-between items-stretch gap-4 flex-1 relative min-h-0 mt-12">
               <img 
                 src="/CatUi.png" 
                 alt="Sitting Cat" 
                 className="absolute -top-[105px] right-8 w-[140px] h-[140px] object-contain z-40 drop-shadow-md pointer-events-none"
               />
               <div className="bg-[#faeec8] border-2 border-black p-4 w-64 flex flex-col shadow-md z-10">
                <h3 className="text-center text-xl text-[#8b1a1a] mb-2 border-b-2 border-black pb-2">Customer List</h3>
                <div className="flex-1 overflow-y-auto font-sans font-normal text-[#a64d1f]">
                  {sortedCustomers.map((cust, idx) => (
                    <p key={cust.id} className={`p-1 border-b border-black/20 ${idx % 2 === 0 ? '' : 'bg-black/5'}`}>
                      {cust.name}
                    </p>
                  ))}
                </div>
                <div className="mt-4">
                  <input 
                    type="text" 
                    placeholder="Search Name/Loc..." 
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="w-full border border-black p-2 text-sm bg-white font-normal"
                  />
                </div>
              </div>

              <div className="flex-1 bg-[#faeec8]/90 border-2 border-black shadow-md overflow-hidden flex flex-col z-10">
                <div className="overflow-auto flex-1">
                  <table className="w-full text-center border-collapse">
                    <thead className="bg-[#cdebf9] border-b-2 border-black text-[#8b1a1a] sticky top-0">
                      <tr>
                        <th className="p-3 border-r border-black">Customer</th>
                        <th className="p-3 border-r border-black">Location</th>
                        <th className="p-3 border-r border-black">Product</th>
                        <th className="p-3 border-r border-black">Weight</th>
                        <th className="p-3 border-r border-black">Value</th>
                        <th className="p-3">Quantity</th>
                      </tr>
                    </thead>
                    <tbody className="bg-white/50 font-sans font-normal">
                      {displayedCustomers.map((cust) => (
                        <tr key={cust.id} className="border-b border-black/20 hover:bg-white/80 transition-colors">
                          <td className="p-3 border-r border-black font-bold">{cust.name}</td>
                          <td className="p-3 border-r border-black">{cust.location}</td>
                          <td className="p-3 border-r border-black">{cust.productName}</td>
                          <td className="p-3 border-r border-black">{cust.weight} kg</td>
                          <td className="p-3 border-r border-black font-bold text-green-700">£{cust.value}</td>
                          <td className="p-3">{cust.quantity}</td>
                        </tr>
                      ))}
                      {displayedCustomers.length === 0 && (
                        <tr>
                          <td colSpan={6} className="p-8 text-xl text-gray-500 font-bold italic">No magic here! Try a different search.</td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>

            <div className="flex justify-between items-center mt-2 shrink-0">
              <button 
                onClick={() => setCurrentScreen('knapsack')}
                className="px-4 py-1.5 bg-white border-2 border-black rounded-full text-sm font-bold text-[#8b1a1a] hover:bg-gray-100 transition-colors"
                >
                Back
              </button>
              <button 
                onClick={handleStartTSP}
                className="px-6 py-1.5 bg-[#faeec8] border-[3px] border-black rounded-full text-base font-bold text-[#8b1a1a] hover:bg-white transition-colors shadow-[3px_3px_0_0_#1c305c] hover:translate-x-[3px] hover:translate-y-[3px] hover:shadow-none"
                >
                Simulate Delivery
              </button>
            </div>
          </motion.div>
        )}

        {currentScreen === 'tsp' && (
           <motion.div
            key="tsp"
            initial={{ opacity: 0, x: 50 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -50 }}
            className="z-10 bg-[#faeec8] border-4 border-black rounded-xl p-4 w-full h-[85vh] max-w-6xl shadow-xl flex flex-col gap-4 relative"
          >
            <h1 className="text-3xl text-center text-[#8b1a1a] mt-2 tracking-widest uppercase">GHIBLI MAP - DELIVERY SIMULATION</h1>
            
            <div className="flex-1 w-full border-4 border-[#1c305c] bg-[#8cd3ff] rounded-lg relative overflow-hidden shadow-inner">
               <svg className="absolute inset-0 w-full h-full pointer-events-none z-0">
                 {tspRoute.map((loc, i) => {
                     if (i === 0 || i > simulationStep) return null;
                     const prev = LOCATIONS[tspRoute[i - 1]];
                     const curr = LOCATIONS[loc];
                     return (
                         <motion.line
                           key={`line-${i}`}
                           x1={`${prev.x}%`} y1={`${prev.y}%`}
                           x2={`${curr.x}%`} y2={`${curr.y}%`}
                           stroke="white" strokeWidth="4" strokeDasharray="8 8"
                           initial={{ pathLength: 0 }}
                           animate={{ pathLength: 1 }}
                           transition={{ duration: 1 }}
                         />
                     );
                 })}
               </svg>
               {Object.entries(LOCATIONS).map(([name, coords]) => {
                 const isVisited = tspRoute.slice(0, simulationStep).includes(name);
                 const isCurrent = tspRoute[simulationStep] === name;
                 const isHome = name === 'Koriko City';
                 const isTarget = tspRoute.includes(name);

                 return (
                     <div 
                       key={name}
                       className="absolute flex flex-col items-center -translate-x-1/2 -translate-y-1/2 z-10"
                       style={{ left: `${coords.x}%`, top: `${coords.y}%` }}
                     >
                         <div className={`w-4 h-4 rounded-full border-2 border-black shadow-md ${isHome ? 'bg-red-500 w-6 h-6' : isCurrent ? 'bg-yellow-400 scale-150' : isVisited ? 'bg-green-400' : isTarget ? 'bg-white' : 'bg-gray-400 opacity-50'}`} />
                         <span className="bg-white/90 px-2 py-0.5 mt-1 text-sm font-bold border-2 border-black rounded shadow text-[#8b1a1a] whitespace-nowrap">{name}</span>
                     </div>
                 );
               })}
               
               {tspRoute.length > 0 && (
                 <motion.img
                   src="/kikinobg.gif"
                   alt="Kiki Flying"
                   className="absolute -translate-x-1/2 -translate-y-1/2 w-24 h-24 object-contain z-20 drop-shadow-[0_0_15px_rgba(255,255,255,0.5)]"
                   animate={{
                       left: `${LOCATIONS[tspRoute[simulationStep]].x}%`,
                       top: `${LOCATIONS[tspRoute[simulationStep]].y}%`
                   }}
                   transition={{ duration: 1, type: 'tween' }}
                 />
               )}
            </div>
            
            <div className="flex justify-between items-center bg-white border-2 border-black p-2 rounded-lg shadow">
               <div className="text-base font-bold flex-1 flex flex-wrap gap-2 items-center">
                 <span className="text-[#8b1a1a]">Route:</span>
                 {tspRoute.map((stop, i) => (
                   <span key={i} className={`flex items-center gap-2 ${i <= simulationStep ? 'text-green-700' : 'text-gray-400'}`}>
                     {stop} {i < tspRoute.length - 1 && <span>➔</span>}
                   </span>
                 ))}
               </div>
               <div className="flex gap-4">
                 <button 
                   onClick={() => setCurrentScreen('intro1')}
                   className="px-4 py-1.5 bg-gray-200 border-2 border-black text-[#8b1a1a] text-sm font-bold rounded-lg hover:bg-gray-300 transition-colors"
                 >
                   Restart
                 </button>
                 <button 
                     onClick={() => {
                       if (simulationStep >= tspRoute.length - 1) {
                         setCurrentScreen('exit');
                         setShowCredits(false);
                       } else {
                         setSimulationStep(s => Math.min(s + 1, tspRoute.length - 1));
                       }
                     }}
                     className="px-6 py-1.5 bg-[#dcb18c] border-2 border-black rounded-lg text-base font-black text-[#682414] hover:bg-opacity-80 disabled:opacity-50 disabled:cursor-not-allowed"
                 >
                    {simulationStep >= tspRoute.length - 1 ? 'Finish Deliveries' : 'Next Stop'}
                 </button>
               </div>
            </div>
           </motion.div>
        )}

        {currentScreen === 'exit' && (
           <motion.div
            key="exit"
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            exit={{ opacity: 0, scale: 1.05 }}
            className="z-10 w-full h-[85vh] max-w-6xl shadow-xl flex flex-col items-center justify-center relative overflow-hidden rounded-xl border-4 border-black"
          >
             <div className="z-10 flex flex-col items-center gap-4 text-white drop-shadow-[0_4px_4px_rgba(0,0,0,0.4)]">
                <motion.img 
                  src="/kikinobg.gif" 
                  alt="Kiki Flying" 
                  className="w-48 h-48 object-contain mb-4 drop-shadow-[0_4px_4px_rgba(0,0,0,0.4)]" 
                  animate={{ y: [0, -15, 0] }}
                  transition={{ duration: 3, repeat: Infinity, ease: "easeInOut" }}
                />
                {!showCredits ? (
                    <>
                        <h1 className="text-3xl md:text-5xl font-sans tracking-wide text-center">All deliveries are completed!</h1>
                        <h1 className="text-2xl md:text-4xl font-sans tracking-wide mt-4 text-center">Great job helping Kiki today!</h1>
                    </>
                ) : (
                    <motion.div 
                        initial="hidden"
                        animate="visible"
                        variants={{
                            hidden: { opacity: 0 },
                            visible: { opacity: 1, transition: { staggerChildren: 0.2 } }
                        }}
                        className="flex flex-col items-center"
                    >
                        <motion.h2 variants={{ hidden: { opacity: 0, y: 10 }, visible: { opacity: 1, y: 0 }}} className="text-2xl font-sans tracking-wide uppercase mb-4 drop-shadow-md">Programmers</motion.h2>
                        <div className="flex flex-col items-center gap-2 text-xl font-sans tracking-wider">
                            <motion.p variants={{ hidden: { opacity: 0, y: 10 }, visible: { opacity: 1, y: 0 }}}>Bai Sakina Abad</motion.p>
                            <motion.p variants={{ hidden: { opacity: 0, y: 10 }, visible: { opacity: 1, y: 0 }}}>Precy Baguio</motion.p>
                            <motion.p variants={{ hidden: { opacity: 0, y: 10 }, visible: { opacity: 1, y: 0 }}}>Saira Sofia De Mesa</motion.p>
                            <motion.p variants={{ hidden: { opacity: 0, y: 10 }, visible: { opacity: 1, y: 0 }}}>Julia Rodrigo</motion.p>
                            <motion.p variants={{ hidden: { opacity: 0, y: 10 }, visible: { opacity: 1, y: 0 }}} className="mt-4 font-black text-2xl">2BSCS-2</motion.p>
                        </div>
                    </motion.div>
                )}
            </div>

            <button 
              onClick={() => {
                if (!showCredits) {
                  setShowCredits(true);
                } else {
                  setCurrentScreen('intro1');
                }
              }}
              className="absolute bottom-8 px-4 py-2 bg-[#dcb18c] border-2 border-black rounded-full text-base font-bold font-sans text-[#8b1a1a] hover:bg-white transition-colors shadow-lg z-20"
            >
              Next
            </button>
           </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
}
