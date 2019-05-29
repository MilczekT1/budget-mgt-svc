package pl.konradboniecki.budget.budgetmanagement.goal.controller;

//@RestController
//@RequestMapping(value = "api/jar")
public class JarController {

//    @Autowired
//    private AccountRepository accountRepository;
//    @Autowired
//    private JarRepository jarRepository;
//    @Autowired
//    private BudgetRepository budgetRepository;

//    @GetMapping("/create-jar")
//    public ModelAndView createJar(ModelMap modelMap){
//        modelMap.put("newJarCreationForm", new JarCreationForm());
//        return new ModelAndView(JAR_CREATION_PAGE, modelMap);
//    }
//
//    @PostMapping
//    public ModelAndView createJar(){
//        if (bindingResult.hasErrors()) {
//            return new ModelAndView(JAR_CREATION_PAGE);
//        }
//        //TODO: retrieve from account service
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        Optional<Account> accOpt = accountRepository.findByEmail(email);
//        if (!accOpt.isPresent()){
//            throw new RuntimeException("Account doesn't exist");
//        }
//        Account acc = accOpt.get();
//
//        Optional<Budget> budgetOpt = budgetRepository.findByFamilyId(acc.getFamilyId());
//        if (!budgetOpt.isPresent()){
//            throw new RuntimeException("Budget doesn't exist");
//        }
//        Budget budget = budgetOpt.get();
//
//        List<Jar> jarList = jarRepository.findAllByBudgetId(budget.getId());
//        if (jarList.size() < budget.getMaxJars()) {
//            Jar jar = new Jar(jarCreationForm);
//            jar.setBudgetId(budget.getId());
//            jarRepository.save(jar);
//            jarList = jarRepository.findAllByBudgetId(budget.getId());
//            modelMap.addAttribute("jarList", jarList);
//            return new ModelAndView(BUDGET_HOME_PAGE, modelMap);
//        } else {
//            modelMap.put("maxJarsAmountExceeded", true);
//            modelMap.put("jarList", jarList);
//            return new ModelAndView(BUDGET_HOME_PAGE, modelMap);
//        }
//    }

//    @PostMapping("/remove-jar")
//    public ModelAndView removeJarFromBudget(@RequestParam("jarId") Long jarId, ModelMap modelMap){
//        jarRepository.deleteById(jarId);
//        return new ModelAndView("redirect:/" + BUDGET_HOME_PAGE, modelMap);
//    }
//
//    @PostMapping("/change-current-amount")
//    public ModelAndView changeCurrentAmountInJarWithId(
//            @RequestParam("jarId") Long jarId,
//            @RequestParam("amount") Long amount,
//            ModelMap modelMap){
//
//        Optional<Jar> jarOpt = jarRepository.findById(jarId);
//        if (jarOpt.isPresent()){
//            Jar jar = jarOpt.get();
//            jar.setCurrentAmount(jar.getCurrentAmount() + amount);
//            jarRepository.save(jar);
////            Long newAmount = jar.getCurrentAmount() + amount;
////            jarRepository.setCurrentAmount(newAmount, jar.getId());
//            return new ModelAndView("redirect:/" + BUDGET_HOME_PAGE, modelMap);
//        } else {
//            return new ModelAndView(ERROR_PAGE, modelMap);
//        }
//
//    }
}